import json

"""
JSON does not support comments, but various languages support JSON literal representations that
allow for comment characters in the literal representation. We explore Python here.

Note: The json package is the primary Python JSON manipulation mechanism, where two concepts are important:
      o Encoding Python data that is JSON compatible is called serialization, similar to marshaling.
      o Decoding serialized JSON data is deserialization, the reciprocal of encoding data that
        conforms to the JSON standard.
"""


def _encodeDecode(label, value):
    """
    :param label: input_str explanation
    :param value: the value to encode/decode
    :return: the decoded value
    """
    print(label, value)
    encoded_value = json.dumps(python_data)
    print("As dumps [encoded]   :", encoded_value)
    decoded_value = json.loads(encoded_value)
    print("As loads [decoded]   :", decoded_value)
    return decoded_value


if __name__ == '__main__':
    print("JSON Object Manipulation Example")
    python_data = {
        "foo": "Chocolate is good",
        "bar": [1, 2, 3, 4.7E3],  # A python comment
        "blatz": (True, False)
    }
    _encodeDecode("Python Object        :", python_data)

    print("\nTry encoding WITHOUT the comment!")
    json_str = """{
        "foo": "Chocolate is good",
        "bar": [1, 2, 3, 4.7E3],
        "blatz": (True, False)
    }"""
    _encodeDecode("Str without comment", json_str)

    print("\nTry encoding WITH the comment!")
    json_str = """{
        "foo": "Chocolate is good",
        "bar": [1, 2, 3, 4.7E3],  # A python comment
        "blatz": (True, False)
    }"""
    _encodeDecode("Str with comment", json_str)

    print("\nUse Python representation of a JSON object with Eval")
    json_str = """{
        "foo": "Chocolate is good",
        "bar": [1, 2, 3, 4.7E3],  # A python comment
        "blatz": (True, False)
    }"""
    print("Python Eval  Str[raw]:", json_str)
    eval_expr = eval(json_str)
    print("              result:", eval_expr)
