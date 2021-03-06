package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.AddressBook;

/**
 * Servlet implementation class AddressBookNavigationServlet
 */
@WebServlet("/addressBookNavigationServlet")
public class AddressBookNavigationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static AddressBookHelper dao = new AddressBookHelper();
	private static String path = "/viewAllAddressBooksServlet";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddressBookNavigationServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String command = request.getParameter("command");

		if (command == null) {
			System.out.println("Nothing Selected");
			sendToNextPage(request, response, path);
		} else {
			switch (command) {
			case "add":
				addAddressBook();
				break;
			case "delete":
				deleteAddressBook(request,response);
				break;
			case "edit":
				editAddressBook(request,response);
			default:
				break;
			}
		}

		sendToNextPage(request, response, path);
	}

	private void sendToNextPage(HttpServletRequest request, HttpServletResponse response, String path) {
		try {
			getServletContext().getRequestDispatcher(path).forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

	public AddressBook searchAddressBooks(HttpServletRequest request, HttpServletResponse response) {
		Integer tempId = null;
		try {
			tempId = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("No Address Book was selected.");
			sendToNextPage(request, response, path);
		}
		return dao.searchForAddressBookById(tempId);
	}

	public void deleteAddressBook(HttpServletRequest request, HttpServletResponse response) {
		dao.deleteAddressBook(searchAddressBooks(request,response));
		path = "/viewAllAddressBooksServlet";
	}

	public void editAddressBook(HttpServletRequest request, HttpServletResponse response) {
		AddressBook toEdit = searchAddressBooks(request,response);
		request.setAttribute("addressBookToEdit", toEdit);
		request.setAttribute("month", toEdit.getCreationDate().getMonth());
		request.setAttribute("day", toEdit.getCreationDate().getDayOfMonth());
		request.setAttribute("year", toEdit.getCreationDate().getYear());

		ContactHelper contactDao = new ContactHelper();
		request.setAttribute("allContacts", contactDao.showAllEntries());

		if (contactDao.showAllEntries().isEmpty()) {
			request.setAttribute("allContacts", " ");
		}
		path = "/edit-address-book.jsp";
	}

	public void addAddressBook() {
		path = "/generateContactListForAddressBookServlet";
	}
}
