package com.spring.model;

import java.util.List;

public class AgentPage {
	private int maxPage;
	
	private List<AgentPagination> agents;

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public List<AgentPagination> getAgents() {
		return agents;
	}

	public void setAgents(List<AgentPagination> agents) {
		this.agents = agents;
	}
	
}
