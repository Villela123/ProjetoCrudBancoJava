package br.com.vinicius.frames;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import br.com.vinicius.dal.ConnectionModule;
import net.proteanit.sql.DbUtils;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Clientes extends JFrame {
	
	
	Connection con = ConnectionModule.conector();
	PreparedStatement pst = null;
	ResultSet rs = null;

	private JPanel contentPane;
	private JTextField txtPesquisar;
	private JTable tblClientes;
	private JTextField txtNome;
	private JTextField txtfone1;
	private JTextField txtEmail;
	private JTextField txtId;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Clientes frame = new Clientes();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Clientes() {
		setResizable(false);
		setTitle("Contato");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 663, 564);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtPesquisar = new JTextField();
		txtPesquisar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				pesquisarCliente();
			}
		});
		txtPesquisar.setBounds(20, 23, 242, 20);
		contentPane.add(txtPesquisar);
		txtPesquisar.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("*Campos Obrigatorios");
		lblNewLabel_1.setBounds(444, 14, 153, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNome = new JLabel("* Nome");
		lblNome.setBounds(10, 240, 48, 14);
		contentPane.add(lblNome);
		
		txtNome = new JTextField();
		txtNome.setBounds(57, 234, 366, 20);
		contentPane.add(txtNome);
		txtNome.setColumns(10);
		
		JLabel lblFone = new JLabel("* Fone");
		lblFone.setBounds(10, 293, 48, 14);
		contentPane.add(lblFone);
		
		txtfone1 = new JTextField();
		txtfone1.setBounds(57, 290, 129, 20);
		contentPane.add(txtfone1);
		txtfone1.setColumns(10);
		
		JLabel lblEmail = new JLabel("* Email");
		lblEmail.setBounds(10, 265, 48, 14);
		contentPane.add(lblEmail);
		
		txtEmail = new JTextField();
		txtEmail.setBounds(57, 262, 577, 20);
		contentPane.add(txtEmail);
		txtEmail.setColumns(10);
		
		JButton bntAdicionar = new JButton("");
		bntAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adicionarCliente();
			}
		});
		bntAdicionar.setIcon(new ImageIcon(Clientes.class.getResource("/br/com/vinicius/icones/create.png")));
		bntAdicionar.setToolTipText("Adicionar Cliente");
		bntAdicionar.setBounds(205, 443, 64, 64);
		contentPane.add(bntAdicionar);
		
		JButton buttonEditar = new JButton("");
		buttonEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				alterar();
			}
		});
		buttonEditar.setIcon(new ImageIcon(Clientes.class.getResource("/br/com/vinicius/icones/update.png")));
		buttonEditar.setToolTipText("Editar Cliente");
		buttonEditar.setBounds(287, 443, 64, 64);
		contentPane.add(buttonEditar);
		
		JButton btnDeletar = new JButton("");
		btnDeletar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deletar();
			}
		});
		btnDeletar.setIcon(new ImageIcon(Clientes.class.getResource("/br/com/vinicius/icones/delete.png")));
		btnDeletar.setToolTipText("Deletar Cliente");
		btnDeletar.setBounds(379, 443, 64, 64);
		contentPane.add(btnDeletar);
		
		JButton button = new JButton("");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pesquisarCliente();
			}
		});
		button.setIcon(new ImageIcon(Clientes.class.getResource("/br/com/vinicius/icones/search.png")));
		button.setBounds(272, 14, 41, 35);
		contentPane.add(button);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 54, 624, 155);
		contentPane.add(scrollPane);
		
		tblClientes = new JTable();
		tblClientes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setarCampos();
			}
		});
		scrollPane.setColumnHeaderView(tblClientes);
		
		txtId = new JTextField();
		txtId.setColumns(10);
		txtId.setBounds(444, 234, 53, 20);
		contentPane.add(txtId);
		
		JLabel lblId = new JLabel("Id");
		lblId.setBounds(507, 240, 48, 14);
		contentPane.add(lblId);
	}//Fim do construtor
	private void limpar() {
		txtId.setText(null);
		txtNome.setText(null);
		txtEmail.setText(null);
		txtfone1.setText(null);
		txtPesquisar.setText(null);
		while (tblClientes.getRowCount() > 0) {
			((DefaultTableModel) tblClientes.getModel()).removeRow(0);
		}
	}
		
	private void adicionarCliente() {
		String create = "insert into pessoas (nome,email,Telefone) values(?,?,?)";
		try {
			pst = (PreparedStatement) con.prepareStatement(create);
			// passagem de parâmetros
			pst.setString(1, txtNome.getText());
			pst.setString(2, txtEmail.getText());
			pst.setString(3, txtfone1.getText());

			int r = pst.executeUpdate();
			if (r > 0) {
				JOptionPane.showMessageDialog(null, "Contato adicionado com sucesso");
				limpar();
			} // fim do if
			else {
				JOptionPane.showMessageDialog(null, "Não foi possivel cadastrar");
			} // fim do else
		} // fim do try
		catch (Exception e) {
			System.out.println(e);
		} // fim do catch

	}// fim do construtor
	
	private void pesquisarCliente() {
        String read = "select * from pessoas where nome like ?";
        try {
            pst = con.prepareStatement(read);
            //atenção ao "%" - continuação da String sql
            pst.setString(1, txtPesquisar.getText() + "%");
            rs = pst.executeQuery();
            // a linha abaixo usa a biblioteca rs2xml.jar para preencher a tabela
            tblClientes.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
			System.out.println(e);
		}
    }
	
	public void setarCampos() {
		int setar = tblClientes.getSelectedRow();
		txtNome.setText(tblClientes.getModel().getValueAt(setar, 1).toString());
		txtEmail.setText(tblClientes.getModel().getValueAt(setar, 2).toString());
		txtfone1.setText(tblClientes.getModel().getValueAt(setar, 3).toString());
		txtId.setText(tblClientes.getModel().getValueAt(setar, 0).toString());
	}
	private void alterar() {
		String alterar = "UPDATE pessoas set nome=?,email=?,Telefone=? where id=?";
		try {
			pst = con.prepareStatement(alterar);
			// passagem de parametros
			pst.setString(1, txtNome.getText());
			pst.setString(2, txtEmail.getText());
			pst.setString(3, txtfone1.getText());
			pst.setString(4, txtId.getText());
			int r = pst.executeUpdate();
			if (r > 0) {
				JOptionPane.showMessageDialog(null, "Contato alterado com sucesso");
				limpar();
			} else {
				JOptionPane.showMessageDialog(null, "Não foi possivel alterar o contato");

			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}
	
	
	private void deletar(){
		int confirma = JOptionPane.showConfirmDialog(null, "Você tem certeza que deseja excluir esse contato?","Atenção",JOptionPane.YES_NO_OPTION);
		if (confirma == JOptionPane.YES_OPTION) {
		String delete = "DELETE FROM pessoas WHERE id=?";
		try {
			pst = con.prepareStatement(delete);
			pst.setString(1, txtId.getText());
			int r = pst.executeUpdate();
			if (r > 0) {
				JOptionPane.showMessageDialog(null, "Contato Deletado com sucesso");
				limpar();
			} 
			else {
				JOptionPane.showMessageDialog(null, "Não foi possivel Deletar o contato");

			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	}
	
	}

