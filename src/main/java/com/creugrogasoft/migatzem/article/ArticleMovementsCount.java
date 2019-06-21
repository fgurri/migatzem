package com.creugrogasoft.migatzem.article;

public class ArticleMovementsCount {

	private int quantitat;
	private long count;
	
	public ArticleMovementsCount(int quantitat, long count) {
		this.quantitat = quantitat;
		this.count = count;
	}
	
	public int getQuantitat() {
		return quantitat;
	}
	public void setQuantitat(int quantitat) {
		this.quantitat = quantitat;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	
}
