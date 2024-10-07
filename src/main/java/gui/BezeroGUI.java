package gui;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import businessLogic.BLFacade;
import domain.Booking;
import domain.Complaint;
import domain.Traveler;

public class BezeroGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private static BLFacade appFacadeInterface;
	private JTable taula;
	private JButton jButtonBaloratu;
	private JButton jButtonErreklamatu;
	private JButton jButtonClose;
	private JScrollPane scrollPane;

	public static BLFacade getBusinessLogic() {
		return appFacadeInterface;
	}

	public static void setBussinessLogic(BLFacade afi) {
		appFacadeInterface = afi;
	}

	public BezeroGUI(String username) {

		setBussinessLogic(DriverGUI.getBusinessLogic());
		this.setSize(new Dimension(600, 537));
		this.setResizable(false);
		getContentPane().setLayout(new BorderLayout(0, 0));

		// Lista
		taula = new JTable();
		List<Booking> TravelsList = appFacadeInterface.getBookingFromDriver(username);
		List<Booking> BezeroLista = new ArrayList<>();

		scrollPane = new JScrollPane(taula);
		getContentPane().add(scrollPane, BorderLayout.NORTH);

		// Etiketak
		String[] columnNames = { ResourceBundle.getBundle("Etiquetas").getString("EgoeraGUI.BookingNumber"),
				ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.RideDate"),
				ResourceBundle.getBundle("Etiquetas").getString("BezeroGUI.Bezeroa"),
				ResourceBundle.getBundle("Etiquetas").getString("EgoeraGUI.Egoera"),
				ResourceBundle.getBundle("Etiquetas").getString("BezeroGUI.Zergatia") };
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		if (TravelsList != null) {
			for (Booking bo : TravelsList) {
				addBooking(model, BezeroLista, bo, dateFormat);
			}
		}
		taula.setModel(model);

		taula.getTableHeader().setReorderingAllowed(false);
		taula.setColumnSelectionAllowed(false);
		taula.setRowSelectionAllowed(true);
		taula.setDefaultEditor(Object.class, null);
		taula.setModel(model);

		// Erroreen textua
		JLabel lblErrorea = new JLabel();
		this.getContentPane().add(lblErrorea, BorderLayout.CENTER);
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("BezeroGUI.Bezeroak"));

		// Baloratu botoia
		jButtonBaloratu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("BezeroGUI.Baloratu"));
		jButtonBaloratu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				baloratu(lblErrorea, BezeroLista, model);
			}
		});

		this.getContentPane().add(jButtonBaloratu, BorderLayout.WEST);

		// Erraklamazio botoia
		jButtonErreklamatu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("BezeroGUI.Onartu") + " / "
				+ ResourceBundle.getBundle("Etiquetas").getString("BezeroGUI.Erreklamatu"));
		jButtonErreklamatu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				erreklamatu(lblErrorea, BezeroLista);
			}

		});

		this.getContentPane().add(jButtonErreklamatu, BorderLayout.EAST);

		// Itxi botoia
		jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("EgoeraGUI.Close"));
		jButtonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonClose_actionPerformed(e);
			}
		});
		this.getContentPane().add(jButtonClose, BorderLayout.SOUTH);

	}

	private void addBooking(DefaultTableModel model, List<Booking> BezeroLista, Booking bo,
			SimpleDateFormat dateFormat) {
		String status;
		switch (bo.getStatus()) {
		case "Completed":
			status = ResourceBundle.getBundle("Etiquetas").getString("Completed");
			break;
		case "Accepted":
			status = ResourceBundle.getBundle("Etiquetas").getString("Accepted");
			break;
		case "Rejected":
			status = ResourceBundle.getBundle("Etiquetas").getString("Rejected");
			break;
		case "NotCompleted":
			status = ResourceBundle.getBundle("Etiquetas").getString("NotCompleted");
			break;
		case "Complained":
			status = ResourceBundle.getBundle("Etiquetas").getString("Complained");
			break;
		case "Valued":
			status = ResourceBundle.getBundle("Etiquetas").getString("Valued");
			break;
		default:
			status = ResourceBundle.getBundle("Etiquetas").getString("NotDefined");
			break;
		}
		if (bo.getStatus().equals("NotCompleted")) {
			Complaint er = appFacadeInterface.getComplaintsByBook(bo);
			if (er != null) {
				if (er.getAurkeztua()) {
					er.setEgoera("Erreklamazioa");
				} else {
					er.setEgoera("Ez aurkeztua");
				}
				Object[] rowData = { bo.getBookNumber(), dateFormat.format(bo.getRide().getDate()),
						bo.getTraveler().getUsername(), status, er.getEgoera() };
				model.addRow(rowData);
				BezeroLista.add(bo);
			}
		} else if (bo.getStatus().equals("Completed") || bo.getStatus().equals("Valued")
				|| bo.getStatus().equals("Complained")) {
			Object[] rowData = { bo.getBookNumber(), dateFormat.format(bo.getRide().getDate()),
					bo.getTraveler().getUsername(), status, "" };
			model.addRow(rowData);
			BezeroLista.add(bo);
		}
	}

	private void baloratu(JLabel lblErrorea, List<Booking> BezeroLista, DefaultTableModel model) {
		int pos = taula.getSelectedRow();
		if (pos != -1) {
			Booking bo = BezeroLista.get(pos);
			if (bo.getStatus().equals("Completed")) {
				bo.setStatus("Valued");
				appFacadeInterface.updateBooking(bo);
				JFrame a = new BaloraGUI(bo.getTraveler().getUsername());
				a.setVisible(true);
				model.setValueAt(ResourceBundle.getBundle("Etiquetas").getString("Valued"), pos, 3);
			} else if (bo.getStatus().equals(ResourceBundle.getBundle("Etiquetas").getString("Valued"))) {
				mezua(lblErrorea,
						ResourceBundle.getBundle("Etiquetas").getString("BezeroGUI.BezeroaJadanikBaloratuta"));
			} else {
				mezua(lblErrorea, ResourceBundle.getBundle("Etiquetas").getString("BezeroGUI.AukeratuOsatutakoBidaia"));
			}
		} else {
			mezua(lblErrorea, ResourceBundle.getBundle("Etiquetas").getString("BezeroGUI.Erroraukera"));
		}
	}

	private void erreklamatu(JLabel lblErrorea, List<Booking> BezeroLista) {
		int pos = taula.getSelectedRow();
		if (pos != -1) {
			Booking booking = BezeroLista.get(pos);
			String complaintStatus = (String) taula.getValueAt(pos, 4);
			erreklamazioMezua(booking, complaintStatus, lblErrorea);
		} else {
			mezua(lblErrorea, ResourceBundle.getBundle("Etiquetas").getString("BezeroGUI.Erroraukera"));
		}
	}

	private void erreklamazioMezua(Booking booking, String egoera, JLabel lblErrorea) {
		if (!egoera.equals("")) {
			double prez = booking.prezioaKalkulatu();
			if (egoera.equals("Erreklamazioa")) {
				erreklamazioOnartua(booking, prez, lblErrorea);
			} else if (egoera.equals("Ez aurkeztua")) {
				erreklamazioOsatuta(booking, prez, lblErrorea);
			} else {
				mezua(lblErrorea,
						ResourceBundle.getBundle("Etiquetas").getString("BezeroGUI.AukeratuEzOsatutakoBidaia"));
			}
		} else if (booking.getStatus().equals(ResourceBundle.getBundle("Etiquetas").getString("Complained"))) {
			mezua(lblErrorea, ResourceBundle.getBundle("Etiquetas").getString("BezeroGUI.BezeroaErreklamazioa"));
		} else {
			mezua(lblErrorea, ResourceBundle.getBundle("Etiquetas").getString("BezeroGUI.AukeratuEzOsatutakoBidaia"));
		}
	}

	private void erreklamazioOnartua(Booking booking, double prez, JLabel lblErrorea) {
		lblErrorea.setText(ResourceBundle.getBundle("Etiquetas").getString("BezeroGUI.ComplaintAccepted"));
		lblErrorea.setForeground(Color.BLACK);
	}

	private void erreklamazioOsatuta(Booking booking, double prez, JLabel lblErrorea) {
		lblErrorea.setText(ResourceBundle.getBundle("Etiquetas").getString("BezeroGUI.ComplaintComplete"));
		lblErrorea.setForeground(Color.BLACK);
	}

	private void mezua(JLabel lblErrorea, String message) {
		lblErrorea.setForeground(Color.RED);
		lblErrorea.setText(message);
	}

	private void jButtonClose_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
}