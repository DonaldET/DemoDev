import json

"""
JSON format has no comment support, but languages using JSON do . . . sort of. Here is an example of Python partially
supporting comments in a JSON string meant to represent a JSON data element.

Notes:
  o encoding JSON ==> serialization for marshaling
  o decoding JSON ==> deserialization as part of unmarshaling
"""

if __name__ == '__main__':
    print("Hi - JSON Data Representation Workout")

    python_data = {"foo" : "Chocolate is good",
            "bar" : [1, 2, "3"],        # A Python, not JSON, comment
            "blatz" : (True, False)
                   }
    print("Raw Python Object: ", python_data)
    encoded_data = json.dumps(python_data)
    print("As dumps [encoded]:", encoded_data)
    decoded_data = json.loads(encoded_data)
    print("As loads [decoded]:", decoded_data)

    json_str = """{
    "foo" : "Chocolate is good",
    "bar" : [1, 2, "3"], # A python comment
    "blatz" : (true, false) 
    }"""
    print("\nJSON Raw String:", json_str)
    decoded_data = json.loads(json_str)
    print("\nJSON string loads [decoded]:", decoded_data)
