import requests
import os
import json

# To set your enviornment variables in your terminal run the following line:
# export 'BEARER_TOKEN'='<your_bearer_token>'


def create_headers(bearer_token):
    headers = {"Authorization": "Bearer {}".format(bearer_token)}
    return headers


def get_rules(headers, bearer_token):
    response = requests.get(
        "https://api.twitter.com/2/tweets/search/stream/rules", headers=headers
    )
    if response.status_code != 200:
        raise Exception(
            "Cannot get rules (HTTP {}): {}".format(response.status_code, response.text)
        )
    print(json.dumps(response.json()))
    return response.json()


def delete_all_rules(headers, bearer_token, rules):
    if rules is None or "data" not in rules:
        return None

    ids = list(map(lambda rule: rule["id"], rules["data"]))
    payload = {"delete": {"ids": ids}}
    response = requests.post(
        "https://api.twitter.com/2/tweets/search/stream/rules",
        headers=headers,
        json=payload
    )
    if response.status_code != 200:
        raise Exception(
            "Cannot delete rules (HTTP {}): {}".format(
                response.status_code, response.text
            )
        )
    print(json.dumps(response.json()))


def set_rules(headers, delete, bearer_token):
    # You can adjust the rules if needed
    sample_rules = [
        {"value": "Enphase Energy", "tag": "Enphase Energy"},
        {"value": "VERBUND AG", "tag": "VERBUND AG"},
        {"value": "Daqo New", "tag": "Daqo New Energy"},
        {"value": "Meridian Energy", "tag": "Meridian Energy Limited"},
        {"value": "Siemens Gamesa", "tag": "Siemens Gamesa"},
        # {"value": "Contact Energy", "tag": "Contact Energy"},
        {"value": "Orsted", "tag": "Orsted"},
        {"value": "Vestas Wind", "tag": "Vestas Wind Systems"},
        {"value": "Ormat Technologies", "tag": "Ormat Technologies"},
        {"value": "Xinyi Solar", "tag": "Xinyi Solar Holdings"},
        # {"value": "First Solar", "tag": "First Solar"},
        # {"value": "Scatec", "tag": "Scatec"},
        # {"value": "EDP Renovaveis", "tag": "EDP Renovaveis"},
        # {"value": "Boralex Inc.", "tag": "Boralex Inc."},
        # {"value": "SolarEdge Technologies", "tag": "SolarEdge Technologies"},
        # {"value": "Sunrun Inc", "tag": "Sunrun Inc"},
        # {"value": "Innergex Renewable Energy", "tag": "Innergex Renewable Energy"},
        # {"value": "Atlantica Sustainable Infrastructure ", "tag": "Atlantica Sustainable Infrastructure "},
        # {"value": "Encavis AG", "tag": "Encavis AG"},
        # {"value": "Neoen S.A", "tag": "Neoen S.A"},
        # {"value": "Companhia Energetica de Minas Gerais", "tag": "Companhia Energetica de Minas Gerais"},
        # {"value": "Doosan Fuel Cell Co", "tag": "Doosan Fuel Cell Co"},
        # {"value": "Canadian Solar Inc.", "tag": "Canadian Solar Inc."},
        # {"value": "Solaria Energia y Medio Ambiente", "tag": "Solaria Energia y Medio Ambiente"},
        # {"value": "Renewable Energy Group", "tag": "Renewable Energy Group"},
        # {"value": "PowerCell Sweden", "tag": "PowerCell Sweden"},
        # {"value": "Companhia Paranaense de Energia", "tag": "Companhia Paranaense de Energia"},
        # {"value": "PowerCell Sweden", "tag": "PowerCell Sweden"},
        # {"value": "Enlight Renewable Energy", "tag": "Enlight Renewable Energy"},
    ]
    payload = {"add": sample_rules}
    response = requests.post(
        "https://api.twitter.com/2/tweets/search/stream/rules",
        headers=headers,
        json=payload,
    )
    if response.status_code != 201:
        raise Exception(
            "Cannot add rules (HTTP {}): {}".format(response.status_code, response.text)
        )
    print(json.dumps(response.json()))


def get_stream(headers, set, bearer_token):
    tweet_count = 15
    response = requests.get(
        "https://api.twitter.com/2/tweets/search/stream", headers=headers, stream=True,
    )
    print(response.status_code)
    if response.status_code != 200:
        raise Exception(
            "Cannot get stream (HTTP {}): {}".format(
                response.status_code, response.text
            )
        )
    for response_line in response.iter_lines():
        if response_line:
            json_response = json.loads(response_line)
            # print(json.dumps(json_response, indent=4, sort_keys=True))
            tweet_count = tweet_count +  1
            file_name = "twitter{}.json".format(tweet_count)
            with open(file_name, 'w') as outfile:
              json.dump(json_response, outfile)


def main():
    bearer_token = os.environ.get("BEARER_TOKEN")
    headers = create_headers(bearer_token)
    rules = get_rules(headers, bearer_token)
    delete = delete_all_rules(headers, bearer_token, rules)
    set = set_rules(headers, delete, bearer_token)
    get_stream(headers, set, bearer_token)


if __name__ == "__main__":
    main()