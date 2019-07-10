package com.spring.dao;

import org.springframework.stereotype.Repository;

import com.spring.model.TicketCount;
import com.spring.model.TicketData;

@Repository
public class TicketDataDao extends ParentDao{
	public TicketData getTicketData(String companyId) {
		TicketData ticket = new TicketData();
		try {
			System.out.println("dao");
//		String hql = "SELECT tbl_company.id, tbl_company.company_name, "
//				+ "(SELECT COUNT(tbl_ticket.*) AS open FROM tbl_ticket WHERE tbl_ticket.status = 'open'), "
//				+ "(SELECT COUNT(tbl_ticket.*) AS closed FROM tbl_ticket WHERE tbl_ticket.status = 'close'), "
//				+ "(SELECT COUNT(tbl_ticket.*) AS reopen FROM tbl_ticket WHERE tbl_ticket.status = 'reopen') "
//				+ "FROM tbl_ticket "
//				+ "JOIN tbl_customer ON tbl_customer.id=tbl_ticket.customer_id "
//				+ "JOIN tbl_company ON tbl_company.id=tbl_customer.company_id "
//				+ "GROUP BY tbl_company.id, tbl_company.company_name";

			String hql = "SELECT tbl_company.id, tbl_company.company_name, "
					+ "(SELECT count(tbl_ticket.*) AS open FROM tbl_ticket "
					+ "JOIN tbl_customer ON tbl_customer.id=tbl_ticket.customer_id "
					+ "JOIN tbl_company ON tbl_company.id=tbl_customer.company_id "
					+ "WHERE tbl_ticket.status = 'open' AND tbl_company.id = :companyId), "
					+ "(SELECT count(tbl_ticket.*) AS closed FROM tbl_ticket "
					+ "JOIN tbl_customer ON tbl_customer.id=tbl_ticket.customer_id "
					+ "JOIN tbl_company ON tbl_company.id=tbl_customer.company_id "
					+ "WHERE tbl_ticket.status = 'close' AND tbl_company.id = :companyId), "
					+ "(SELECT count(tbl_ticket.*) AS reopen FROM tbl_ticket "
					+ "JOIN tbl_customer ON tbl_customer.id=tbl_ticket.customer_id "
					+ "JOIN tbl_company ON tbl_company.id=tbl_customer.company_id "
					+ "WHERE tbl_ticket.status = 'reopen' AND tbl_company.id = :companyId) " + "FROM tbl_ticket "
					+ "JOIN tbl_customer ON tbl_customer.id=tbl_ticket.customer_id "
					+ "JOIN tbl_company ON tbl_company.id=tbl_customer.company_id "
					+ "WHERE tbl_company.id = :companyId "
					+ "GROUP BY tbl_company.id, tbl_company.company_name";

//		List<Object[]> ticks = super.entityManager.createNativeQuery(hql).getResultList();
//		for(Object[] obj:ticks)
//		{	
//			System.out.println(obj[0]);
//			System.out.println(obj[1]);
//			System.out.println(obj[2]);
//			System.out.println(obj[3]);
//		}

			ticket = (TicketData) super.entityManager.createNativeQuery(hql, TicketData.class)
					.setParameter("companyId", companyId).getSingleResult();

			System.out.println(ticket.getCompanyName());
			System.out.println(ticket.getOpen());
			System.out.println(ticket.getClosed());
			System.out.println(ticket.getReopen());

			return ticket;
		} catch (Exception e) {
			return ticket;
		}
	}
	
	public TicketData test(String companyId) {
		TicketData ticket = new TicketData();
		try {
			System.out.println("dao");
			System.out.println(companyId);

			String hql = "SELECT tbl_company.id, tbl_company.company_name, "
					+ "(SELECT count(tbl_ticket.*) AS open FROM tbl_ticket "
					+ "JOIN tbl_customer ON tbl_customer.id=tbl_ticket.customer_id "
					+ "JOIN tbl_company ON tbl_company.id=tbl_customer.company_id "
					+ "WHERE tbl_ticket.status = 'open' AND tbl_company.id = :companyId), "
					+ "(SELECT count(tbl_ticket.*) AS closed FROM tbl_ticket "
					+ "JOIN tbl_customer ON tbl_customer.id=tbl_ticket.customer_id "
					+ "JOIN tbl_company ON tbl_company.id=tbl_customer.company_id "
					+ "WHERE tbl_ticket.status = 'close' AND tbl_company.id = :companyId), "
					+ "(SELECT count(tbl_ticket.*) AS reopen FROM tbl_ticket "
					+ "JOIN tbl_customer ON tbl_customer.id=tbl_ticket.customer_id "
					+ "JOIN tbl_company ON tbl_company.id=tbl_customer.company_id "
					+ "WHERE tbl_ticket.status = 'reopen' AND tbl_company.id = :companyId) " + "FROM tbl_ticket "
					+ "JOIN tbl_customer ON tbl_customer.id=tbl_ticket.customer_id "
					+ "JOIN tbl_company ON tbl_company.id=tbl_customer.company_id "
					+ "WHERE tbl_company.id = :companyId "
					+ "GROUP BY tbl_company.id, tbl_company.company_name";

			ticket = (TicketData) super.entityManager.createNativeQuery(hql, TicketData.class)
					.setParameter("companyId", companyId).getSingleResult();

			System.out.println(ticket.getCompanyName());
			System.out.println(ticket.getOpen());
			System.out.println(ticket.getClosed());
			System.out.println(ticket.getReopen());

			return ticket;
		} catch (Exception e) {
			return ticket;
		}
	}
	
	public TicketCount countAll() {
		TicketCount ticketCount = new TicketCount();
		try {
			System.out.println("dao");
		String hql="SELECT (SELECT count(tbl_ticket.*) AS open FROM tbl_ticket WHERE tbl_ticket.status = 'open'), "
				+ "(SELECT count(tbl_ticket.*) AS closed FROM tbl_ticket WHERE tbl_ticket.status = 'close'), "
				+ "(SELECT count(tbl_ticket.*) AS reopen FROM tbl_ticket WHERE tbl_ticket.status = 'reopen') "
				+ "FROM tbl_ticket LIMIT 1";
		
		Object[] object = (Object[]) super.entityManager.createNativeQuery(hql)
				.getSingleResult();
		
		int open = Integer.parseInt(object[0].toString());
		int closed = Integer.parseInt(object[1].toString());
		int reopen = Integer.parseInt(object[2].toString());

		ticketCount.setOpen(open);
		ticketCount.setReopen(reopen);
		ticketCount.setClosed(closed);
				
		return ticketCount;
		}
		catch(Exception e) {
			return null;
		}
	}
}
