package data;

public class Data {
    private StockExchange stockExchange;
    private Session[] sessions;

    public Data(StockExchange stockExchange, Session[] sessions){
        this.stockExchange = stockExchange;
        this.sessions = sessions;
    }

    public Session[] getSessions(){
        return sessions;
    }

    public StockExchange getStockExchange() {
        return stockExchange;
    }
}

