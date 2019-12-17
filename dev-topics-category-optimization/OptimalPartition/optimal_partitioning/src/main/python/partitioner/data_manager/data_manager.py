"""
data_manager.py

Support organizing data for partition analysis
"""

from partitioner import observation_manager


# def _assemble_data(label, input_data, collected_data):
#     assert collected_data is not None
#     input_data = [observation_manager.Observation(float(x)) for x in input_data]
#     collected_data.append(input_data)
#     print('{:s} = {:s}'.format(label, str(input_data)))
#     return input_data, collected_data


def assemble_data(input_data, collected_data, label=None):
    """
    Convert input data (sample_observations) to floating point values and accumulate
        label: Optional label for printing the input
        input_data: the input data to convert to Observation instances
        collected_data: the prior list of accumulated Observation instances that is modified with additional input
    Returns: Floating representation of input
    """
    assert input_data is not None
    assert len(input_data) > 0
    assert collected_data is not None

    float_input = [float(x) for x in input_data]
    if label is not None:
        print('Group: {:s} = {:s} ==> {:s}'.format(label, str(input_data), str(float_input)))

    observations = [observation_manager.Observation(x) for x in float_input]
    collected_data.append(observations)

    return float_input


def load_example1():
    observations = observation_manager.ObservedValues()
    samples = []

    data, samples = assemble_data('A', [10, 15, 8, 12, 15], samples)
    observations.add_observations(data)

    data, samples = assemble_data('B', [14, 18, 21, 15], samples)
    observations.add_observations(data)

    data, samples = assemble_data('C', [17, 16, 14, 15, 17, 15, 18], samples)
    observations.add_observations(data)

    data, samples = assemble_data('D', [12, 15, 17, 15, 16, 15], samples)
    observations.add_observations(data)

    group_n = [float(len(x)) for x in samples]

    return group_n, samples, observations
