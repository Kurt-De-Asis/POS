package de.asis;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;

public class POS extends JFrame {

    JLabel label = new JLabel("Select Item: ");
    JTextField text1 = new JTextField();
    JButton b1 = new JButton("ADD ITEM");
    JButton checkoutButton = new JButton("CHECKOUT");
    JButton insertitemlist = new JButton("INSERT ITEM");
    JButton voidAllItemsButton = new JButton("VOID ALL ITEMS");
    JSpinner jp = new JSpinner();
    JLabel label2 = new JLabel("QUANTITY:");
    JTextField newItemField = new JTextField();
    JTextField newPriceField = new JTextField();

    // Table for available items
    DefaultTableModel availableItemsTable = new DefaultTableModel();
    JTable availableItemsT = new JTable(availableItemsTable);
    JScrollPane availableItemsSP = new JScrollPane(availableItemsT);

    // Table for added items
    DefaultTableModel addedItemsTable = new DefaultTableModel();
    JTable addedItemsT = new JTable(addedItemsTable);
    JScrollPane addedItemsSP = new JScrollPane(addedItemsT);

    JLabel totalLabel = new JLabel("Total Price: ");
    JTextField totalPriceField = new JTextField("0");

    String[] items = {"LEMON", "C2", "YAKULT", "COKE", "COKE ZERO", "SPRITE", "ROYALE", "MINUTE MADE", "TOBLERONE", "CADBURY DAIRY MILK"};
    int[] price1 = {30, 15, 10, 50, 55, 60, 65, 35, 125, 180};
    int totalPrice = 0;

    public POS() {
        // Main frame setup
        setTitle("Item Sales");
        setSize(800, 600);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Setup available items table
        availableItemsTable.addColumn("Item Name");
        availableItemsTable.addColumn("Price");

        for (int i = 0; i < items.length; i++) {
            availableItemsTable.addRow(new Object[]{items[i], price1[i]});
        }

        label.setBounds(10, 20, 100, 20);
        text1.setBounds(120, 20, 150, 20);
        jp.setBounds(120, 50, 60, 30);
        label2.setBounds(10, 55, 80, 20);
        b1.setBounds(10, 90, 100, 50);
        availableItemsSP.setBounds(10, 150, 270, 200);
        addedItemsSP.setBounds(300, 150, 250, 200);
        totalLabel.setBounds(300, 370, 100, 20);
        totalPriceField.setBounds(400, 370, 150, 20);
        totalPriceField.setEditable(false);
        checkoutButton.setBounds(120, 90, 120, 50);
        insertitemlist.setBounds(250, 90, 140, 50);
        voidAllItemsButton.setBounds(400, 90, 150, 50);

        // Fields to insert a new item
        newItemField.setBounds(400, 20, 150, 20);
        newPriceField.setBounds(400, 50, 150, 20);
        JLabel newItemLabel = new JLabel("New Item:");
        JLabel newPriceLabel = new JLabel("New Price:");
        newItemLabel.setBounds(320, 20, 100, 20);
        newPriceLabel.setBounds(320, 50, 100, 20);

        // Add components to the frame
        add(label);
        add(text1);
        add(jp);
        add(label2);
        add(b1);
        add(availableItemsSP);
        add(addedItemsSP);
        add(totalLabel);
        add(totalPriceField);
        add(checkoutButton);
        add(insertitemlist);
        add(voidAllItemsButton);
        add(newItemField);
        add(newPriceField);
        add(newItemLabel);
        add(newPriceLabel);

        // Setup added items table
        addedItemsTable.addColumn("Item Name");
        addedItemsTable.addColumn("Quantity");
        addedItemsTable.addColumn("Price");

        // Add mouse listener to available items table
        availableItemsT.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = availableItemsT.getSelectedRow();
                if (row >= 0) {
                    String selectedItem = availableItemsTable.getValueAt(row, 0).toString();
                    text1.setText(selectedItem);
                }
            }
        });

        // Action listener for the ADD ITEM button
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = text1.getText();
                int quantity = (Integer) jp.getValue();
                boolean itemFound = false;

                // Check if the item exists in the available items list
                for (int i = 0; i < availableItemsTable.getRowCount(); i++) {
                    if (availableItemsTable.getValueAt(i, 0).toString().equalsIgnoreCase(selectedItem)) {
                        int price = Integer.parseInt(availableItemsTable.getValueAt(i, 1).toString()) * quantity;
                        totalPrice += price;

                        // Add item to the added items table
                        addedItemsTable.addRow(new Object[]{selectedItem, quantity, price});

                        // Update total price
                        totalPriceField.setText(String.valueOf(totalPrice));

                        itemFound = true;
                        break;
                    }
                }

                if (!itemFound) {
                    JOptionPane.showMessageDialog(POS.this, "Item not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action listener for the CHECKOUT button
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder receipt = new StringBuilder();
                receipt.append("Receipt:\n\n");

                // Loop through added items to create a receipt
                for (int i = 0; i < addedItemsTable.getRowCount(); i++) {
                    String itemName = (String) addedItemsTable.getValueAt(i, 0);
                    int quantity = (Integer) addedItemsTable.getValueAt(i, 1);
                    int price = (Integer) addedItemsTable.getValueAt(i, 2);
                    receipt.append(itemName).append(" (Quantity: ").append(quantity).append(") - Price: ").append(price).append("\n");
                }
                receipt.append("\nTotal Price: ").append(totalPrice);

                // Display receipt in a dialog
                JOptionPane.showMessageDialog(POS.this, receipt.toString(), "Receipt", JOptionPane.INFORMATION_MESSAGE);

                // Reset the added items table and total price after checkout
                addedItemsTable.setRowCount(0);
                totalPrice = 0;
                totalPriceField.setText("0");
            }
        });

        // Action listener for the INSERT ITEM button
        insertitemlist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newItem = newItemField.getText();
                String newPriceText = newPriceField.getText();
                try {
                    int newPrice = Integer.parseInt(newPriceText);

                    // Add new item to the available items table
                    availableItemsTable.addRow(new Object[]{newItem, newPrice});

                    // Clear input fields after insertion
                    newItemField.setText("");
                    newPriceField.setText("");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(POS.this, "Invalid price", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action listener for the VOID ALL ITEMS button
        voidAllItemsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset the added items table and total price
                addedItemsTable.setRowCount(0);
                totalPrice = 0;
                totalPriceField.setText("0");
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new POS();
    }
}
