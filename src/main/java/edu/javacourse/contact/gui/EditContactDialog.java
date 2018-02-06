package edu.javacourse.contact.gui;

import edu.javacourse.contact.entity.Contact;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

public class EditContactDialog extends JDialog implements ActionListener
{
    private static final String DIALOG = "dialog";
    private static final String C_GIVEN = "givenname";
    private static final String C_SURNAME = "surname";
    private static final String C_PHONE = "phone";
    private static final String C_EMAIL = "email";
    // Заголовки кнопок
    private static final String SAVE = "SAVE";
    private static final String CANCEL = "CANCEL";

    // Размер отступа
    private static final int PAD = 10;
    // Ширина метки
    private static final int W_L = 100;
    //Ширина поля для ввода
    private static final int W_T = 300;
    // Ширина кнопки
    private static final int W_B = 120;
    // высота элемента - общая для всех
    private static final int H_B = 25;

    // Поле для ввода Имени
    private final JTextPane txtFirstName = new JTextPane();
    // Поле для ввода Фамилии
    private final JTextPane txtLastName = new JTextPane();
    // Поле для ввода Телефона
    private final JTextPane txtPhone = new JTextPane();
    // Поле для ввода E-mail
    private final JTextPane txtEmail = new JTextPane();

    // Поле для хранения ID контакта, если мы собираемся редактировать
    // Если это новый контакт - cjntactId == null
    private Long contactId = null;
    // Надо ли записывать изменения после закрытия диалога
    private boolean save = false;

    public EditContactDialog() {
        this(null);
    }

    public EditContactDialog(Contact contact) {
        // Убираем layout - будем использовать абсолютные координаты
        setLayout(null);

        // Выстраиваем метки и поля для ввода
        buildFields();
        // Если нам передали контакт - заполняем поля формы
        initFields(contact);
        // выстариваем кнопки
        buildButtons();

        // Диалог в модальном режиме - только он активен
        setModal(true);
        // Запрещаем изменение размеров
        setResizable(false);
        // Выставляем размеры формы
        setBounds(300, 300, 450, 200);
        // Делаем форму видимой
        setVisible(true);
    }

    // Размещаем метки и поля ввода на форме
    private void buildFields() {
        // Набор метки и поля для Имени
        JLabel lblFirstName = new JLabel(GuiResource.getLabel(DIALOG, C_GIVEN) + ":");
        // Выравнивание текста с правой стороны
        lblFirstName.setHorizontalAlignment(SwingConstants.RIGHT);
        // Выставляем координаты метки
        lblFirstName.setBounds(new Rectangle(PAD, 0 * H_B + PAD, W_L, H_B));
        // Кладем метку на форму
        add(lblFirstName);
        // Выставляем координаты поля
        txtFirstName.setBounds(new Rectangle(W_L + 2 * PAD, 0 * H_B + PAD, W_T, H_B));
        // Делаем "бордюр" для поля
        txtFirstName.setBorder(BorderFactory.createEtchedBorder());
        // Кладем поле на форму
        add(txtFirstName);

        // Набор метки и поля для Фамилии
        JLabel lblLastName = new JLabel(GuiResource.getLabel(DIALOG, C_SURNAME) + ":");
        lblLastName.setHorizontalAlignment(SwingConstants.RIGHT);
        lblLastName.setBounds(new Rectangle(PAD, 1 * H_B + PAD, W_L, H_B));
        add(lblLastName);
        txtLastName.setBounds(new Rectangle(W_L + 2 * PAD, 1 * H_B + PAD, W_T, H_B));
        txtLastName.setBorder(BorderFactory.createEtchedBorder());
        add(txtLastName);

        // Набор метки и поля для Телефона
        JLabel lblPhone = new JLabel(GuiResource.getLabel(DIALOG, C_PHONE) + ":");
        lblPhone.setHorizontalAlignment(SwingConstants.RIGHT);
        lblPhone.setBounds(new Rectangle(PAD, 2 * H_B + PAD, W_L, H_B));
        add(lblPhone);
        txtPhone.setBounds(new Rectangle(W_L + 2 * PAD, 2 * H_B + PAD, W_T, H_B));
        txtPhone.setBorder(BorderFactory.createEtchedBorder());
        add(txtPhone);

        // Набор метки и поля для Email
        JLabel lblEmail = new JLabel(GuiResource.getLabel(DIALOG, C_EMAIL) + ":");
        lblEmail.setHorizontalAlignment(SwingConstants.RIGHT);
        lblEmail.setBounds(new Rectangle(PAD, 3 * H_B + PAD, W_L, H_B));
        add(lblEmail);
        txtEmail.setBounds(new Rectangle(W_L + 2 * PAD, 3 * H_B + PAD, W_T, H_B));
        txtEmail.setBorder(BorderFactory.createEtchedBorder());
        add(txtEmail);
    }

    // Если нам епередали контакт - заполняем поля из контакта
    private void initFields(Contact contact) {
        if (contact != null) {
            contactId = contact.getContactId();
            txtFirstName.setText(contact.getFirstName());
            txtLastName.setText(contact.getLastName());
            txtEmail.setText(contact.getEmail());
            txtPhone.setText(contact.getPhone());
        }
    }

    // Размещаем кнопки на форме
    private void buildButtons() {
        JButton btnSave = new JButton("SAVE");
        btnSave.setActionCommand(SAVE);
        btnSave.addActionListener(this);
        btnSave.setBounds(new Rectangle(PAD, 5 * H_B + PAD, W_B, H_B));
        add(btnSave);

        JButton btnCancel = new JButton("CANCEL");
        btnCancel.setActionCommand(CANCEL);
        btnCancel.addActionListener(this);
        btnCancel.setBounds(new Rectangle(W_B + 2 * PAD, 5 * H_B + PAD, W_B, H_B));
        add(btnCancel);
    }

    @Override
    // Обработка нажатий кнопок
    public void actionPerformed(ActionEvent ae) {
        String action = ae.getActionCommand();
        // Если нажали кнопку SAVE (сохранить изменения) - запоминаем этой
        save = SAVE.equals(action);
        // Закрываем форму
        setVisible(false);
    }

    // Надо ли сохранять изменения
    public boolean isSave() {
        return save;
    }

    // Создаем контакт из заполенных полей, который можно будет записать
    public Contact getContact() {
        Contact contact = new Contact(contactId, txtFirstName.getText(),
                txtLastName.getText(), txtPhone.getText(), txtEmail.getText());
        return contact;
    }
}
