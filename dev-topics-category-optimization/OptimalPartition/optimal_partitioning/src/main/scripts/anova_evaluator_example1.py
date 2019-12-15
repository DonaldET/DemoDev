"""
anova_evaluator_example1.py
"""

import anova_evaluator
import observation_manager


def _assemble_data(label, group_observations, old_samples):
    assert old_samples is not None
    group_observations = [observation_manager.Observation(float(x)) for x in group_observations]
    old_samples.append(group_observations)
    print('{:s} = {:s}'.format(label, str(group_observations)))
    return group_observations, old_samples


def _load_example1():
    observations = observation_manager.ObservedValues()
    samples = []

    data, samples = _assemble_data('A', [10, 15, 8, 12, 15], samples)
    observations.add_observations(data)

    data, samples = _assemble_data('B', [14, 18, 21, 15], samples)
    observations.add_observations(data)

    data, samples = _assemble_data('C', [17, 16, 14, 15, 17, 15, 18], samples)
    observations.add_observations(data)

    data, samples = _assemble_data('D', [12, 15, 17, 15, 16, 15], samples)
    observations.add_observations(data)

    group_n = [float(len(x)) for x in samples]

    return group_n, samples, observations


def _calculate_sums(k_categories, k_category_counts, samples):
    trt_sumx = []
    for k_cat in range(k_categories):
        samp_k = samples[k_cat]
        assert len(samp_k) == k_category_counts[k_cat]
        trt_sumx.append(sum(x.value for x in samp_k))

    trt_sumx2 = []
    for k_cat in range(k_categories):
        samp_k = samples[k_cat]
        assert len(samp_k) == k_category_counts[k_cat]
        trt_sumx2.append(sum(x.value * x.value for x in samp_k))

    trt_mean = []
    for k_cat in range(k_categories):
        trt_mean.append(trt_sumx[k_cat] / k_category_counts[k_cat])
    u_grand = sum(trt_sumx) / sum(k_category_counts)

    # Residual Sum of squares
    ss_res = []
    for k_cat in range(k_categories):
        ss_res.append(trt_sumx2[k_cat] - trt_sumx[k_cat] * trt_sumx[k_cat] / k_category_counts[k_cat])

    # Definitional Residual Sum of squares
    ss_res_def = []
    for k_cat in range(k_categories):
        samp_k = samples[k_cat]
        ss_res_def.append(sum([(x.value - trt_mean[k_cat]) ** 2 for x in samp_k]))

    # Treatment SS
    trt_ss = []
    for k_cat in range(k_categories):
        trt_effect = trt_mean[k_cat] - u_grand
        trt_ss.append(k_category_counts[k_cat] * trt_effect * trt_effect)

    # Definitional Treatment SS
    trt_ssd = []
    for k_cat in range(k_categories):
        trt_effect = trt_mean[k_cat] - u_grand
        trt_ssd.append(k_category_counts[k_cat] * trt_effect * trt_effect)

    # Total SS
    ss_tot = sum(trt_sumx2) - sum(trt_sumx) ** 2 / sum(k_category_counts)

    # Definitional Total SS
    ss_tot_def = []
    for k_cat in range(k_categories):
        ss_tot_def.append(sum([(x.value - u_grand) ** 2 for x in samples[k_cat]]))
    return trt_sumx, trt_sumx2, trt_mean, ss_res, ss_res_def, trt_ss, trt_ssd, ss_tot, ss_tot_def


# Fake Unit Test
if __name__ == '__main__':
    """
    From http://vassarstats.net/anova1u.html

    Data Summary   S.1     S.2     S.3   S.4     Total
    N                5       4       7     6        22
    SumX            60      68     112    90       330
    Mean            12      17      16    15        15
    SumX2          758    1186    1804  1364      5112
    Variance       9.5      10       2   2.8    7.7143
    Std.Dev.    3.0822  3.1623  1.4142  1.6733  2.7775
    Std.Err.    1.3784  1.5811  0.5345  0.6831  0.5922

    ANOVA Summary           
    Source              SS  df       MS     F         P
    Treatment
    [between groups]   68    3  22.6667  4.34  0.018142
    Error              94   18   5.2222
    Total             162   21
    Eta-Squared                          0.420
    """
    print('\n==== Test ANOVA Example 1 ====')
    Nk, groupings, obs = _load_example1()
    print('\n    --- Loaded Test Values ----')
    print('Observations:', str(obs))
    print('Samples     :', str(groupings))

    K = len(Nk)
    print('\nK: {:.1f};        Nk: {:s}'.format(K, str(Nk)))

    treatment_sumx, treatment_sumx2, treatment_mean, residual_ss, residual_ssd, treatment_ss, \
    treatment_ssd, s_total_ss, total_ssd = _calculate_sums(K, Nk, groupings)

    print('Sum X            : {:s}                    Total: {:.3f}'.format(str(treatment_sumx), sum(treatment_sumx)))
    print('Sum X**2         : {:s}              Total: {:.3f}'.format(str(treatment_sumx2), sum(treatment_sumx2)))
    grand_mean = sum(treatment_sumx) / float(sum(Nk))
    print('Treatment means  : {:s}                Grand Mean: {:.3f}'.format(str(treatment_mean), grand_mean))
    print('----------')
    print('Residual SS      : {:s}         Total Residual SS: {:.3f}'.format(str(residual_ss), sum(residual_ss)))
    print('Residual SS (Def): {:s}         Total Residual SS: {:.3f}'.format(str(residual_ss), sum(residual_ss)))
    print('----------')
    print('Treatment SS     : {:s}                Treatment SS: {:.3f}'.format(str(treatment_ss), sum(treatment_ss)))
    print('Treatment SS(Def): {:s}                Treatment SS: {:.3f}'.format(str(treatment_ss), sum(treatment_ss)))
    print('----------')
    print('Total SS                                                     Total SS: {:.3f}'.format(s_total_ss))
    print('Total SS (Dev)   : {:s}                  Total SS: {:.3f}'.format(str(total_ssd), sum(total_ssd)))
    print('\n===================\n')

    ae = anova_evaluator.AnovaEvaluator(obs)
    print('Created    :\n', str(ae))

    n_in_bins = ae.set_category_counts(Nk)
    print('\nInitialized:\n', str(ae))
