# https://github.com/Azure-Samples/cognitive-services-REST-api-samples/blob/master/python/Search/BingNewsSearchv7.py
# https://docs.microsoft.com/en-us/answers/questions/173628/bing-search-api-returns-the-34resource-not-found34.html


# Copyright (c) Microsoft Corporation. All rights reserved.
# Licensed under the MIT License.

# -*- coding: utf-8 -*-

import json
import os
from pprint import pprint
import requests

'''
This sample makes a call to the Bing News Search API with a text query and returns relevant news webpages.
Documentation: https: // docs.microsoft.com/en-us/azure/cognitive-services/bing-web-search/
'''

# Add your Bing Search V7 subscription key and endpoint to your environment variables.
# subscriptionKey = os.environ['BING_SEARCH_V7_SUBSCRIPTION_KEY']
# endpoint = os.environ['BING_SEARCH_V7_ENDPOINT'] + "/bing/v7.0/news/search"
subscriptionKey = os.environ.get("BING_SEARCH_V7_SUBSCRIPTION_KEY")
# endpoint = os.environ.get("BING_SEARCH_V7_ENDPOINT") + "/bing/v7.0/news/search"
endpoint = "https://api.bing.microsoft.com/v7.0/search"
query = "Microsoft"

# Construct a request
mkt = 'en-US'
params = {'q': query, 'mkt': mkt}
headers = {'Ocp-Apim-Subscription-Key': subscriptionKey}

# Call the API
try:
    response = requests.get(endpoint, headers=headers, params=params)
    response.raise_for_status()

    print("\nHeaders:\n")
    print(response.headers)

    print("\nJSON Response:\n")
    pprint(response.json())
    with open('data_2.json', 'w') as outfile:
		    json.dump(response.json(), outfile)
	  	
except Exception as ex:
    raise ex