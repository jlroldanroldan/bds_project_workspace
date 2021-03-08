from azure.cognitiveservices.search.newssearch import NewsSearchClient
from msrest.authentication import CognitiveServicesCredentials

import json
import os
from pprint import pprint
import requests
import sys

print(sys.path)


# subscription_key = "YOUR-SUBSCRIPTION-KEY"
subscription_key = os.environ.get("BING_SEARCH_V7_SUBSCRIPTION_KEY")
# endpoint = "YOUR-ENDPOINT"
endpoint = os.environ.get("BING_SEARCH_V7_ENDPOINT")
search_term = "Quantum Computing"


client = NewsSearchClient(endpoint=endpoint, credentials=CognitiveServicesCredentials(subscription_key))
news_result = client.news.search(query=search_term, market="en-us", count=10)

if news_result.value:
    first_news_result = news_result.value[0]
    print("Total estimated matches value: {}".format(
        news_result.total_estimated_matches))
    print("News result count: {}".format(len(news_result.value)))
    print("First news name: {}".format(first_news_result.name))
    print("First news url: {}".format(first_news_result.url))
    print("First news description: {}".format(first_news_result.description))
    print("First published time: {}".format(first_news_result.date_published))
    print("First news provider: {}".format(first_news_result.provider[0].name))
else:
    print("Didn't see any news result data..")