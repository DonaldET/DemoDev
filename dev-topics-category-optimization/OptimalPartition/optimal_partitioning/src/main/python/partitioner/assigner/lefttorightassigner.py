from partitioner.assigner.abstract_assigner import AbstractAssigner, Collector

# #################################################################################### #
# Copyright (c) 2019. Donald E. Trummell. All Rights Reserved.                         #
# Permission to use, copy, modify, and distribute this software and its documentation  #
# for educational, research, and not-for-profit purposes, without fee and without      #
# a signed licensing agreement, is hereby granted, provided that the above             #
# copyright notice, and this paragraph, appear in all copies, modifications, and       #
# distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.   #
# #################################################################################### #

trace = False


class LeftToRightAssigner(AbstractAssigner):
    """
    Create all partitions in a left to right manner, starting with minimum count on the left and building t
     maximum count to the right. Partition count definitions are used to control the generation process.
    """

    def __init__(self, scorer, partition_count, space_size, min_size, max_size):
        super(LeftToRightAssigner, self).__init__(scorer, partition_count, space_size, min_size, max_size)

        self.id_generator = 0
        self.category_counts = None
        self.reserves = [(self.partition_count - i) * self.min_size for i in range(1, self.partition_count + 1)]
        if trace:
            print('-- init: partition_count: {},  space_size: {},  min_size: {},  max_size: {}'.format(
                str(partition_count), str(space_size), str(min_size), str(max_size)))
            print('\n   reserves:', str(self.reserves))
        self.end_ptr = self.partition_count - 1
        self.collector = Collector(scorer)

    def generate_all(self):
        """
        Generate all assignments
        :return: the maximum assignment
        """
        self.collector.reset()
        self.id_generator = 0
        self.category_counts = [0 for _ in range(self.partition_count)]
        last = self._generate_me(self.space_size, 0)

        assert last > 0
        return self.collector.max_assignment

    def _generate_me(self, available, me):
        """
        Iterate through assignments by generating partition counts working left (low) to right (high) in
        partition sizing
        :param available: the number of objects to divide into category_counts
        :param me: the partition position in the assignment
        :return: the last value of the assignment generator
        """
        if me >= self.end_ptr:  # terminal condition
            self.id_generator += 1
            self.category_counts[me] = available
            if trace:
                print(' +++', self.id_generator, 'available:', available,
                      '--' if available < self.min_size else ('++' if available > self.max_size else '  '),
                      'category_counts', str(self.category_counts),
                      'Examined' if available <= self.max_size else ('***' if available < self.min_size else ''))
            if available <= self.max_size:
                self.collector.add_assignment(self.id_generator, self.category_counts, trace)
            return self.id_generator

        for count in range(self.min_size, min(self.max_size, available - self.reserves[me]) + 1):
            self.category_counts[me] = count
            new_available = available - count
            self._generate_me(new_available, me + 1)
        return self.id_generator
