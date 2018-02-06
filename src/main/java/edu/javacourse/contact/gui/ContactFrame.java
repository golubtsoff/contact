package edu.javacourse.contact.gui;

import edu.javacourse.contact.business.ContactManager;
import edu.javacourse.contact.entity.Contact;
import edu.javacourse.contact.exception.ContactBusinessException;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class ContactFrame extends JFrame implements ActionListener
{
    private static final String FRAME = "frame";
    private static final String C_REFRESH = "refresh";
    private static final String C_ADD = "add";
    private static final String C_UPDATE = "update";
    private static final String C_DELETE = "delete";

    private static final String LOAD = "LOAD";
    private static final String ADD = "ADD";
    private static final String EDIT = "EDIT";
    private static final String DELETE = "DELETE";

    private final ContactManager contactManager = new ContactManager();
    private final JTable contactTable = new JTable();

    // В конструкторе мы создаем нужные элементы
    public ContactFrame() {
        // Выставляем у таблицы свойство, которое позволяет выделить
        // ТОЛЬКО одну строку в таблице
        contactTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        // Используем layout GridBagLayout
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        // Каждый элемент является последним в строке
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        // Элемент раздвигается на весь размер ячейки
        gbc.fill = GridBagConstraints.BOTH;
        // Но имеет границы - слева, сверху и справа по 5. Снизу - 0
        gbc.insets = new Insets(5, 5, 0, 5);

        // Создаем панель для кнопок
        JPanel btnPanel = new JPanel();
        // усанавливаем у него layout
        btnPanel.setLayout(gridbag);
        // Создаем кнопки и укзаываем их загловок и ActionCommand
        btnPanel.add(createButton(gridbag, gbc, GuiResource.getLabel(FRAME, C_REFRESH), LOAD));
        btnPanel.add(createButton(gridbag, gbc, GuiResource.getLabel(FRAME, C_ADD), ADD));
        btnPanel.add(createButton(gridbag, gbc, GuiResource.getLabel(FRAME, C_UPDATE), EDIT));
        btnPanel.add(createButton(gridbag, gbc, GuiResource.getLabel(FRAME, C_DELETE), DELETE));

        // Создаем панель для левой колокни с кнопками
        JPanel left = new JPanel();
        // Выставляем layout BorderLayout
        left.setLayout(new BorderLayout());
        // Кладем панель с кнопками в верхнюю часть
        left.add(btnPanel, BorderLayout.NORTH);
        // Кладем панель для левой колонки на форму слева - WEST
        add(left, BorderLayout.WEST);

        // Кладем панель со скролингом, внутри которой нахоится наша таблица
        // Теперь таблица может скроллироваться
        add(new JScrollPane(contactTable), BorderLayout.CENTER);

        // выставляем координаты формы
        setBounds(100, 200, 900, 400);
        // При закрытии формы заканчиваем работу приложения
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Загружаем контакты
        try {
            loadContact();
        } catch (ContactBusinessException ex) {
            ex.printStackTrace();
        }
    }

    // Метод создает кнопку с заданными харктеристиками - заголовок и действие
    private JButton createButton(GridBagLayout gridbag, GridBagConstraints gbc, String title, String action) {
        // Создаем кнопку с заданным загловком
        JButton button = new JButton(title);
        // Действие будетп роверяться в обработчике и мы будем знать, какую 
        // именно кнопку нажали
        button.setActionCommand(action);
        // Обработчиком события от кнопки являемся сама форма
        button.addActionListener(this);
        // Выставляем свойства для размещения для кнопки
        gridbag.setConstraints(button, gbc);
        return button;
    }

    @Override
    // Обработка нажатий кнопок
    public void actionPerformed(ActionEvent ae) {
        // Получаем команду - ActionCommand
        String action = ae.getActionCommand();
        // В зависимости от команды выполняем действия
        try {
            switch (action) {
                // Перегрузка данных
                case LOAD:
                    loadContact();
                    break;
                // Добавление записи
                case ADD:
                    addContact();
                    break;
                // Исправление записи
                case EDIT:
                    editContact();
                    break;
                // Удаление записи
                case DELETE:
                    deleteContact();
                    break;
            }
        } catch (ContactBusinessException ex) {
            // Очень простой способ показать сообщение
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // Загрухить список контактов
    private void loadContact() throws ContactBusinessException {
        // Обращаемся к классу для загрузки списка контактов
        List<Contact> contacts = contactManager.findContacts();
        // Создаем модель, которой передаем полученный список
        ContactModel cm = new ContactModel(contacts);
        // Передаем нашу модель таблице - и она может ее отображать
        contactTable.setModel(cm);
    }

    // Добавление контакта
    private void addContact() throws ContactBusinessException {
        // Создаем диалог для ввода данных
        EditContactDialog ecd = new EditContactDialog();
        // Обрабатываем закрытие диалога
        saveContact(ecd);
    }

    // Редактирование контакта
    private void editContact() throws ContactBusinessException {
        // Получаем выделеннуб строку
        int sr = contactTable.getSelectedRow();
        // если строка выделена - можно ее редактировать
        if (sr != -1) {
            // Получаем ID контакта
            Long id = Long.parseLong(contactTable.getModel().getValueAt(sr, 0).toString());
            // получаем данные контакта по его ID
            Contact cnt = contactManager.getContact(id);
            // Создаем диалог для ввода данных и передаем туда контакт
            EditContactDialog ecd = new EditContactDialog(contactManager.getContact(id));
            // Обрабатываем закрытие диалога
            saveContact(ecd);
        } else {
            // Если строка не выделена - сообщаем об этом
            JOptionPane.showMessageDialog(this, "Вы должны выделить строку для редактирования");
        }
    }

    // Удаление контакта
    private void deleteContact() throws ContactBusinessException {
        // Получаем выделеннуб строку
        int sr = contactTable.getSelectedRow();
        if (sr != -1) {
            // Получаем ID контакта
            Long id = Long.parseLong(contactTable.getModel().getValueAt(sr, 0).toString());
            // Удаляем контакт
            contactManager.deleteContact(id);
            // перегружаем список контактов
            loadContact();
        } else {
            JOptionPane.showMessageDialog(this, "Вы должны выделить строку для удаления");
        }
    }

    // Общий метод для добавления и изменения контакта
    private void saveContact(EditContactDialog ecd) throws ContactBusinessException {
        // Если мы нажали кнопку SAVE
        if (ecd.isSave()) {
            // Получаем контакт из диалогового окна
            Contact cnt = ecd.getContact();
            if (cnt.getContactId() != null) {
                // Если ID у контакта есть, то мы его обновляем
                contactManager.updateContact(cnt);
            } else {
                // Если у контакта нет ID - значит он новый и мы его добавляем
                contactManager.addContact(cnt);
            }
            loadContact();
        }
    }
}