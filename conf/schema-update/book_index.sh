#!/bin/bash

curl -XPUT 'http://localhost:9200/books' -d '{
    "settings": {
        "number_of_shards": 1
    },
    "mappings": {
        "book": {
            "properties": {
                "isbn" : {
                    "type": "string",
                    "index": "not_analyzed"
                },
                "title" : {
                    "type": "string",
                    "index": "analyzed",
                    "fields": {
                        "raw" : {
                            "type": "string",
                            "index": "not_analyzed"
                        }
                    }
                },
                "author": {
                    "type": "string",
                    "index": "analyzed",
                    "fields": {
                        "raw" : {
                            "type": "string",
                            "index": "not_analyzed"
                        }
                    }
                },
                "blurb": {
                    "type": "string"
                },
                "publisher": {
                    "type": "string"
                },
                "publishDate": {
                    "type": "date"
                },
                "genres": {
                    "type": "string",
                    "index_name": "genre"
                }
            }
        }
    }
}'
