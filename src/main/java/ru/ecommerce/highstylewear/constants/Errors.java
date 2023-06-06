package ru.ecommerce.highstylewear.constants;

public interface Errors {
    class Item {
        public static final String ITEM_DELETE_ERROR = "Предмет одежды не может быть удален.";
    }

    class Users {
        public static final String USER_FORBIDDEN_ERROR = "У вас нет прав просматривать эту информацию";
    }

    class REST {
        public static final String DELETE_ERROR_MESSAGE = "Удаление невозможно.";
        public static final String AUTH_ERROR_MESSAGE = "Неавторизованный пользователь.";
        public static final String ACCESS_ERROR_MESSAGE = "Отказано в доступе!";
        public static final String NOT_FOUND_ERROR_MESSAGE = "Объект не найден!";
        public static final String LOGIN_REGISTERED_ERROR_MESSAGE = "Пользователь с таким логином уже зарегистрирован";
        public static final String EMAIL_REGISTERED_ERROR_MESSAGE = "Пользователь с таким адресом электронной почты уже зарегистрирован";
    }
}
