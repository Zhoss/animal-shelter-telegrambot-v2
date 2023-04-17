package pro.sky.teamwork.animalsheltertelegrambotv2.model;

public enum Command {
    START_COMMAND("/start", "Основное меню"),
    SHELTER_INFO_COMMAND("/shelter_info", "Меню с информацией о приюте"),
    SHELTER_MAIN_INFO_COMMAND("/shelter_main_info", "Основная информация о приюте"),
    SHELTER_WORK_SCHEDULE_COMMAND("/shelter_work_schedule", "Информация о расписании работы приюта, адресе, схеме проезда, контактной информации"),
    SHELTER_SAFETY_RECOMMENDATIONS_COMMAND("/shelter_safety_recommendations", "Рекомендации о технике безопасности на территории приюта"),
    WRITE_CLIENT_CONTACT_COMMAND("/write_contact_information", "Записать контактные данные для связи с волонтерами"),
    CALL_VOLUNTEER_COMMAND("/call_volunteer", "Позвать волонтера"),
    BACK_COMMAND("/back", "Вернуться назад"),
    TAKE_A_DOG_COMMAND("/take_dog", "Как взять собаку из приюта"),
    INTRODUCTION_TO_DOG_COMMAND("/intro_dog", "Узнать правила знакомства с собакой"),
    TAKE_DOCUMENTS_LIST_COMMAND("/take_doc_list", "Получить список документов"),
    TRANSFER_A_DOG_COMMAND("/transfer_dog", "Транспортировка животного"),
    ENVIRONMENT_FOR_PUPPY_COMMAND("/puppy_environment", "Обустройство дома для щенка"),
    ENVIRONMENT_FOR_DOG_COMMAND("/dog_environment", "Обустройство дома для взрослой собаки"),
    ENVIRONMENT_FOR_LIMITED_DOG_COMMAND("/limited_dog_environment", "Обустройство дома для собаки с ограниченными возможностями"),
    CYNOLOGIST_ADVICES_COMMAND("/cynologist_advices", "Советы кинолога"),
    CYNOLOGIST_CONTACTS_COMMAND("/cynologist_contacts", "Контакты проверенных кинологов"),
    USUAL_REFUSALS_COMMAND("/usual_refusals", "Частые причины отказов в выдаче собаки кандидату"),
    SEND_REPORT_MENU_COMMAND("/send_report_menu", "Прислать отчет о питомце"),
    SEND_REPORT_COMMAND("/send_report", "Прислать отчет"),
    VOLUNTEER_CONFIRM_COMMAND("/volunteer_confirm", "Подтверждение приема заявки");

    private final String command;
    private final String description;

    Command(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }
}
