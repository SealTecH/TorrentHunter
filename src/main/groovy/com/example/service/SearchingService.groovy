package com.example.service

import com.example.model.FindQueryModel
import org.elasticsearch.action.search.SearchRequestBuilder
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.search.sort.SortBuilders
import org.elasticsearch.search.sort.SortOrder
import org.elasticsearch.transport.client.PreBuiltTransportClient

import org.springframework.stereotype.Service
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.action.search.SearchResponse

@Service
class SearchingService {

    private TransportClient client;
    private FindQueryModel model;
    private SearchRequestBuilder Request;
    public SearchResponse Response;
   /* public SearchingService()
    {

    }*/
    public void setModel(FindQueryModel model)
    {
        this.model =model;
    }


    public void Search() {
        this.OpenConnect();


        this.Request =this.client.prepareSearch("torrents").setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setIndices()
                // .addFields("search", "magnet","categories","tags","peers_updated","seeders","fileSize","seeder_was")
                //.setQuery(QueryBuilders.fieldQuery("search",this.model.getSearchString()))
                .setQuery(QueryBuilders.fuzzyQuery("search",this.model.getSearchString()))
                .setFrom(this.model.getCompletePage()).setSize(100);

        switch (this.model.Sort)
       {
        case 2: this.Request.addSort(SortBuilders.fieldSort("fileSize").order(SortOrder.DESC)); break;
        case 3: this.Request.addSort(SortBuilders.fieldSort("seeders").order(SortOrder.DESC));break;
        case 4: this.Request.addSort(SortBuilders.fieldSort("peers_updated").order(SortOrder.DESC));break;
       }
        println("REQUEST IS ");
        println(this.Request);
        this.Response =this.Request.execute().actionGet();
        this.CloseConnect();
    }

    public void  OpenConnect()
    {
        this.client =new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("104.198.110.70"), 9300));
    }
    public void CloseConnect()
    {
        this.client.close();
    }

}
