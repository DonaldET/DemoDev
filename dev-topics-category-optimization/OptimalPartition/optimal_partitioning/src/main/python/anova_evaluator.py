"""
anova_evaluator.py

This code was first done in Visual Basic by Alan Bostrom at Iameter, later translated and improved
for C++, again translated to Java, and finally appears in this Python version. ANOVA R-squared is computed.
"""

from abstract_scorer import AbstractScorer


#
# ANOVA calculation methods are used to evaluate the "R-squared" statistic associated with a fixed categorization
# (partitioning) of an ordered set of interval-data values.
#
# Notes:
#   -- Sample calculator: http://vassarstats.net/anova1u.html
#   -- Wiki definition of ANOVA: https://en.wikipedia.org/wiki/One-way_analysis_of_variance
#   -- Reference 1: Sample summary including computation suggestions: https://www3.nd.edu/~rwilliam/xsoc63992/x52.pdf
#   -- Reference 2: Textbook: Statistical Concepts and Methods [Bhattacharyya], page 453
#
# Given K treatments, we have a simple one-factor model:
#
# We want to compare the means of K different populations; and we have K samples, each of size N[k]. Any individual
# score can be written as follows:
#
# y[i,j]           = µ           + τ[j]            + ε[i,j],             where j = 1, K (# groups) and i =1..., N[K], or
# Observation[i,j] = Grand Mean  + treatment[j]    + residual[i,j],      or
# y[i,j]           = µ           + (Exp[*,j] - µ)  + (y[i,j] - Exp(*,j), where µ = Exp(Y[*,*])
#

class AnovaEvaluator(AbstractScorer):
    def __init__(self, observed_values):
        assert observed_values is not None
        observations = observed_values.observations
        assert observations is not None
        assert len(observations) > 1
        self.observed_values = observed_values
        self.category_counts = None
        self.treatment_raw_sumx = None
        self.treatment_raw_sumx2 = None
        self.total_ss = None
        self.treatment_ss = None
        self.residual_ss = None
        self.treatment_mean = None
        self.grand_mean = None
        self.treatment_df = None
        self.error_df = None
        self.total_df = None
        self.ms_treatments = None
        self.ms_error = None
        self.f_ratio = None

    def _reset_anova(self):
        assert self.observed_values is not None
        self.category_counts = None
        self.treatment_raw_sumx = None
        self.treatment_raw_sumx2 = None
        self.total_ss = None
        self.treatment_ss = None
        self.residual_ss = None
        self.treatment_mean = None
        self.grand_mean = None
        self.treatment_df = None
        self.error_df = None
        self.total_df = None
        self.ms_treatments = None
        self.ms_error = None
        self.f_ratio = None
        return True

    def _check_counts(self, category_counts):
        k = len(category_counts)
        assert k > 1
        for x in category_counts:
            assert int(x) > 0 and int(x) == float(x)
        self.category_counts = [int(x) for x in category_counts]
        n_in_bins = sum(self.category_counts)
        n_observed = len(self.observed_values.observations)
        assert n_in_bins == n_observed
        return n_observed, k

    def _compute_ss(self):
        """
        basic sums of squares for ANOVA table are:
        SS Total = (SS Treatment, SS Between, SS Explained) + (SS Residual, SS Error, SS Within)
        :return: observation count and category count
        """
        assert self.observed_values is not None
        n_observed = 0
        self.treatment_raw_sumx = []
        self.treatment_raw_sumx2 = []
        lb = 0
        for cnt in self.category_counts:
            hb = lb + cnt
            n_observed += hb - lb
            self.treatment_raw_sumx.append(self._get_sum_x(lb, hb - 1))
            self.treatment_raw_sumx2.append(self._get_sum_x2(lb, hb - 1))
            lb = hb

        self.treatment_mean = []
        k = len(self.category_counts)
        for j in range(k):
            self.treatment_mean.append(self.treatment_raw_sumx[j] / float(self.category_counts[j]))
        g = sum(self.treatment_raw_sumx)
        pop_n = float(n_observed)
        self.grand_mean = g / pop_n
        offset = g * g / pop_n
        self.total_ss = sum(self.treatment_raw_sumx2) - offset
        ss_between = []
        for j in range(k):
            ss_between.append(self.treatment_raw_sumx[j] * self.treatment_raw_sumx[j] / float(self.category_counts[j]))
        self.treatment_ss = sum(ss_between) - offset
        ss_between = []
        for j in range(k):
            ss_between.append(self.treatment_raw_sumx[j] * self.treatment_raw_sumx[j] / float(self.category_counts[j]))
        ss_between = sum(ss_between)
        self.treatment_ss = ss_between - offset
        self.residual_ss = sum(self.treatment_raw_sumx2) - ss_between
        return n_observed, k

    def _get_sum_x(self, low_index, high_index):
        observations = self.observed_values.observations
        return sum([x.value for x in observations[low_index: high_index + 1]])

    def _get_sum_x2(self, low_index, high_index):
        observations = self.observed_values.observations
        return sum([x.value * x.value for x in observations[low_index: high_index + 1]])

    #
    # Computational Notes (from Ref 2, page 461 and Ref 1):
    #
    # Total SS   =  Treatment SS   + Residual SS (Error SS)
    #    or
    # SS Total   =  SS Between     + SS Within
    #              (SS Explained)
    #
    # Because we use these synonyms:
    #   SS Within  = SS Errors     = SS Residual
    #   SS Between = SS Explained
    #

    def _compute_anova_table(self):
        n_observed = self._compute_ss()
        k = len(self.category_counts)
        return n_observed, k

    def compute_f_ratio(self):
        """
        Compute the F-ratio using the SS and MS values
        :return: the F ratio associated with the ANOVA table
        """
        assert self.treatment_raw_sumx is not None
        k = len(self.category_counts)
        self.treatment_df = k - 1
        n_observed = len(self.observed_values.observations)
        self.error_df = n_observed - k
        self.total_df = n_observed - 1
        self.ms_treatments = self.treatment_ss / float(self.treatment_df)

        return 1.33

    def set_category_counts(self, category_counts):
        n_observed, k = self._check_counts(category_counts)
        n_ss, k_ss = self._compute_ss()
        assert n_observed == n_ss
        assert k == k_ss
        return n_observed

    def measure(self, capacity_sequence):
        """
        Score a capacity sequence over prepared data values as eta_squared-squared (ANOVA equivalent of OLS R-squared)
        :param capacity_sequence: an ordered collection of partition capacity values in an assignment
        :return: Eta-squared of associated categories (ratio of explained SS to total SS
        """
        self.set_category_counts(capacity_sequence)
        return self.treatment_ss / self.total_ss

    def __repr__(self):
        msg = '[' + str(self.__class__)
        msg += ';\n     Observations: {:s}'.format(str(self.observed_values))
        if self.category_counts is not None:
            msg += ';\n  Counts      : {:s} = {:d}'.format(str(self.category_counts), sum(self.category_counts))
            msg += ';\n  Sum X       : {:s} = {:.3f}'.format(str(self.treatment_raw_sumx), sum(self.treatment_raw_sumx))
            msg += ';\n  Sum X2      : {:s} = {:.3f}'.format(str(self.treatment_raw_sumx2),
                                                             sum(self.treatment_raw_sumx2))
            msg += ';\n  Means       : {:s} = {:.3f}'.format(str(self.treatment_mean), self.grand_mean)
            txt = ';\n  Total SS    : {:s};    Treatment SS: {:s};    Residual SS : {:s}'
            msg += txt.format(str(self.total_ss), str(self.treatment_ss), str(self.residual_ss))

        if self.treatment_df is not None:
            txt = ';\n  Total df    : {:s};    Treatment df: {:s};    Residual df : {:s}'
            msg += txt.format(str(self.total_df), str(self.treatment_df), str(self.error_df))

        msg += ']'
        return msg

    def __str__(self):
        return self.__repr__()
