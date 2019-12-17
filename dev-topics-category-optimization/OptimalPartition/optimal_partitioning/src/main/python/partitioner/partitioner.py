"""
partitioner.py

Creates selected partitions of a space of size objects into individual assignments; each assignment has a sequence of
partitions of varying sizes (min to max) and an associated  score; a collector records the assignment with the maximum
score (See AA_README.docx in field_validation.)
"""
from partitioner.scorer import sum_square_scorer as sss

trace = False


class Assignment(object):
    """
    An assignment associates a score with a collection of space partitions and an identifier
    """

    def __init__(self, assignment_id, partitions, score):
        """
        Create an assignment with a collection of space partitions, an identifier, and a score
        :param assignment_id: a unique identifier for this assignment
        :param partitions: an ordered collection of capacities
        :param score: a value associated with the partitions
        """
        self.partition_id = assignment_id
        self.partitions = partitions
        self.score = score

    def __str__(self):
        msg = '['
        msg += super(Assignment, self).__str__()
        msg += ' :: '
        msg += ';  '.join(
            ['AssignID: {:s}'.format(str(self.partition_id)),
             'score: {:s}'.format(str(self.score)),
             'capacities: {:s}'.format(str(self.partitions))])
        msg += ']'
        return msg


class Collector(object):
    """
    A collector examines many raw assignments, applies a score to each assignment, and records the assignment with
    the maximum score
    """

    def __init__(self, scorer):
        """
        Examine in-coming assignments and apply a score; record the assignment with the maximum score
        :param scorer:
        """
        assert scorer is not None
        self.max_assignment = None
        self.examined = 0
        self.scorer = scorer

    def reset(self):
        self.max_assignment = None
        self.examined = 0

    def add_assignment(self, assignment_id, capacity_sequence):
        """
        Assess an assignment, apply the score and record the assignment if it is the current max
        :param assignment_id: the identifier to apply to the assignment
        :param capacity_sequence: the ordered sequence of capacities
        :return: the current maximum assignment
        """
        assignment = Assignment(assignment_id, list(capacity_sequence), self.scorer.measure(capacity_sequence))
        if self.max_assignment is None:
            self.max_assignment = assignment
        elif self.max_assignment.score < assignment.score:
            self.max_assignment = assignment
        self.examined += 1
        if trace:
            print('---', assignment_id, assignment.score, '-- max assignment -->', str(self.max_assignment), '----\n')
        return self.max_assignment

    def __str__(self):
        msg = '['
        msg += super(Collector, self).__str__()
        msg += ' :: max_assignment: '
        msg += str(self.max_assignment)
        msg += ';  examined:' + str(self.examined)
        msg += ']'
        return msg


class Space(object):
    """
    A space defines the min and max capacity bounds for a fixed number of partitions over size objects; it also
    associates a collector that scores and records the last generated assignment with the maximum score
    """

    def __init__(self, collector, partition_count, space_size, min_size, max_size):
        assert collector is not None
        self.collector = collector

        assert partition_count > 1
        assert min_size > 0
        assert max_size > min_size
        assert space_size > 0

        assert space_size >= partition_count * min_size
        assert space_size <= partition_count * max_size

        self.partition_count = partition_count
        self.space_size = space_size
        self.min_size = min_size
        self.max_size = max_size
        self.id_generator = 0
        self.capacities = None

        self.reserves = [(self.partition_count - i) * self.min_size for i in range(1, self.partition_count + 1)]
        if trace:
            print('reserves:', str(self.reserves))
        self.end_ptr = self.partition_count - 1

    def generate_all(self):
        """
        Generate all assignments
        :return: the maximum assignment
        """
        self.collector.reset()
        self.id_generator = 0
        self.capacities = [0 for _ in range(self.partition_count)]
        last = self._generate_me(self.space_size, 0)

        assert last > 0
        return self.collector.max_assignment

    def _generate_me(self, available, me):
        """
        Iterate through assignments; generate partition counts working left (low) to right (high) in
        partition sizing
        :param available: the number of objects to divide into partitions
        :param me: the partition position in the assignment
        :return: the last value of the assignment generator
        """
        if me >= self.end_ptr:
            self.id_generator += 1
            self.capacities[me] = available
            if trace:
                print(self.id_generator, 'available:', available,
                      '--' if available < self.min_size else ('++' if available > self.max_size else '  '),
                      'capacities', str(self.capacities),
                      'Examined' if available <= self.max_size else ('***' if available < self.min_size else ''))
            if available <= self.max_size:
                self.collector.add_assignment(self.id_generator, self.capacities)
            return self.id_generator

        for capacity in range(self.min_size, min(self.max_size, available - self.reserves[me]) + 1):
            self.capacities[me] = capacity
            new_available = available - capacity
            self._generate_me(new_available, me + 1)
        return self.id_generator

    def __str__(self):
        msg = '['
        msg += super(Space, self).__str__()
        msg += ' :: '
        msg += ';  '.join(
            ['partition_count: {:d}'.format(self.partition_count),
             'space_size: {:d}'.format(self.space_size),
             'min_size: {:d}'.format(self.min_size),
             'max_size: {:d}'.format(self.max_size),
             'id_generator {:d}'.format(self.id_generator),
             'last capacities: {:s}'.format(
                 'None' if self.capacities is None else '{' + str(self.capacities) + '}')])
        msg += ']'
        return msg


# Fake unit tests
if __name__ == '__main__':
    def _test_runner(test_partition_count, test_space_size, test_min_size, test_max_size):
        test_collector = Collector(sss.SumSquareScorer())
        spc = Space(test_collector, test_partition_count, test_space_size, test_min_size, test_max_size)
        print('  -- Initial :', str(spc))
        spc.generate_all()
        print('  -- Final ID:', str(spc.id_generator))
        return test_collector


    tst_label = 'Two categories - 1/3'
    print('\n\nStarting', tst_label)
    tst_partition_count = 2
    tst_space_size = 4
    tst_min_size = 1
    tst_max_size = 3
    tst_collector = _test_runner(tst_partition_count, tst_space_size, tst_min_size, tst_max_size)
    print('Result:', tst_collector)

    tst_label = 'Two categories - 6/18'
    print('\n\nStarting', tst_label)
    tst_partition_count = 2
    tst_space_size = 17
    tst_min_size = 6
    tst_max_size = 10
    tst_collector = _test_runner(tst_partition_count, tst_space_size, tst_min_size, tst_max_size)
    print('Result:', tst_collector)

    tst_label = 'Three categories - small'
    print('\nStarting', tst_label)
    tst_partition_count = 3
    tst_space_size = 11
    tst_min_size = 1
    tst_max_size = 7
    tst_collector = _test_runner(tst_partition_count, tst_space_size, tst_min_size, tst_max_size)
    print('Result:', tst_collector)

    tst_label = 'Three categories - medium'
    print('\nStarting', tst_label)
    tst_partition_count = 3
    tst_space_size = 37
    tst_min_size = 5
    tst_max_size = 14
    tst_collector = _test_runner(tst_partition_count, tst_space_size, tst_min_size, tst_max_size)
    print('Result:', tst_collector)

    tst_label = 'Three categories - large'
    print('\nStarting', tst_label)
    tst_partition_count = 3
    big_val = 200
    third = int((big_val + 2) // 3)
    tst_space_size = third * 3 + 11
    tst_min_size = third - 6
    tst_max_size = round(1.5 * third)
    tst_collector = _test_runner(tst_partition_count, tst_space_size, tst_min_size, tst_max_size)
    print('Result:', tst_collector)

    tst_label = 'Three categories - giant'
    print('\nStarting', tst_label)
    tst_partition_count = 3
    big_val = 5000
    third = int((big_val + 2) // 3)
    tst_space_size = third * 3 + 113
    tst_min_size = third - 213
    tst_max_size = round(1.5 * third)
    tst_collector = _test_runner(tst_partition_count, tst_space_size, tst_min_size, tst_max_size)
    print('Result:', tst_collector)

    tst_label = 'Five categories'
    print('\nStarting', tst_label)
    tst_partition_count = 5
    tst_space_size = 31
    tst_min_size = 3
    tst_max_size = 7
    tst_collector = _test_runner(tst_partition_count, tst_space_size, tst_min_size, tst_max_size)
    print('Result:', tst_collector)
