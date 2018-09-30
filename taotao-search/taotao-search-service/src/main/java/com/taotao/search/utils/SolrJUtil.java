package com.taotao.search.utils;

import org.apache.solr.client.solrj.impl.HttpSolrClient;

public class SolrJUtil {

private static String BASE_URL="http://101.132.164.6:8983/solr/collection1";
	
	public static HttpSolrClient getSolrClient(){
        /*
         * 设置超时时间
         * .withConnectionTimeout(10000)
         * .withSocketTimeout(60000)
         */
        return new HttpSolrClient.Builder(BASE_URL)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
    }
}
