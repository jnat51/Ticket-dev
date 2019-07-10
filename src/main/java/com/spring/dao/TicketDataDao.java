package com.spring.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.spring.model.TicketData;

@Repository
public class TicketDataDao extends ParentDao{
	public List<TicketData> getTicketData() {
		List<TicketData> tickets = new ArrayList<TicketData>();
		try {
			System.out.println("dao");
		String hql = "SELECT tbl_company.id, tbl_company.company_name, "
				+ "(SELECT COUNT(tbl_ticket.*) AS open FROM tbl_ticket WHERE tbl_ticket.status = 'open'), "
				+ "(SELECT COUNT(tbl_ticket.*) AS closed FROM tbl_ticket WHERE tbl_ticket.status = 'close'), "
				+ "(SELECT COUNT(tbl_ticket.*) AS reopen FROM tbl_ticket WHERE tbl_ticket.status = 'reopen') "
				+ "FROM tbl_ticket "
				+ "JOIN tbl_customer ON tbl_customer.id=tbl_ticket.customer_id "
				+ "JOIN tbl_company ON tbl_company.id=tbl_customer.company_id "
				+ "GROUP BY tbl_company.id, tbl_company.company_name";
		
//		List<Object[]> ticks = super.entityManager.createNativeQuery(hql).getResultList();
//		for(Object[] obj:ticks)
//		{
//			System.out.println(obj[0]);
//			System.out.println(obj[1]);
//			System.out.println(obj[2]);
//			System.out.println(obj[3]);
//		}
		
		tickets = super.entityManager.createNativeQuery(hql, TicketData.class)
				.getResultList();
		
		for(TicketData ticket : tickets) {
			System.out.println(ticket.getCompanyName());
		}

		return tickets;
	} catch (Exception e) {
		return tickets;
	}
	}
}
