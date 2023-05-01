package pro.sky.teamwork.animalsheltertelegrambotv2.model;

public enum Command {
    START_COMMAND("/start", "Основное меню"),
    SELECT_CAT_SHELTER_COMMAND("/select_cat_shelter", "Выбрать приют для кошек"),
    SELECT_DOG_SHELTER_COMMAND("/select_dog_shelter", "Выбрать приют для кошек"),
    SHELTER_INFO_COMMAND("/shelter_info", "Меню с информацией о приюте"),
    SHELTER_MAIN_INFO_COMMAND("/shelter_main_info", "Основная информация о приюте"),
    SHELTER_WORK_SCHEDULE_COMMAND("/shelter_work_schedule", "Информация о расписании работы приюта, адресе, схеме проезда, контактной информации"),
    SECURITY_CONTATCT_COMMAND("/security_contact", "Контактные данные охраны для оформления пропуска на машину"),
    SHELTER_SAFETY_RECOMMENDATIONS_COMMAND("/shelter_safety_recommendations", "Рекомендации о технике безопасности на территории приюта"),
    WRITE_CLIENT_CONTACT_COMMAND("/write_contact_information", "Записать контактные данные для связи с волонтерами"),
    CALL_VOLUNTEER_COMMAND("/call_volunteer", "Позвать волонтера"),
    BACK_COMMAND("/back", "Вернуться назад"),
    TAKE_A_PET_COMMAND("/take_pet", "Как взять питомца из приюта"),
    INTRODUCTION_TO_PET_COMMAND("/intro_pet", "Узнать правила знакомства с питомцем"),
    TAKE_DOCUMENTS_LIST_COMMAND("/take_pet_list", "Получить список документов"),
    TRANSFER_A_PET_COMMAND("/transfer_pet", "Транспортировка животного"),
    ENVIRONMENT_FOR_YOUNG_PET_COMMAND("/young_pet_environment", "Обустройство дома для детеныша"),
    ENVIRONMENT_FOR_PET_COMMAND("/pet_environment", "Обустройство дома для взрослого питомца"),
    ENVIRONMENT_FOR_LIMITED_PET_COMMAND("/limited_pet_environment", "Обустройство дома для питомца с ограниченными возможностями"),
    CYNOLOGIST_ADVICES_COMMAND("/cynologist_advices", "Советы кинолога"),
    CYNOLOGIST_CONTACTS_COMMAND("/cynologist_contacts", "Контакты проверенных кинологов"),
    USUAL_REFUSALS_COMMAND("/usual_refusals", "Частые причины отказов в выдаче питомца кандидату"),
    SEND_REPORT_MENU_COMMAND("/send_report_menu", "Прислать отчет о питомце"),
    SEND_REPORT_COMMAND("/send_report", "Прислать отчет"),
    VOLUNTEER_CONFIRM_COMMAND("/volunteer_confirm", "Подтверждение приема заявки"),
    EXTEND_PROBATION_14_COMMAND("/extend_probation_14", "Продлить испытательный срок на 14 дней"),
    EXTEND_PROBATION_30_COMMAND("/extend_probation_30", "Продлить испытательный срок на 30 дней"),
    PROBATION_PASSED_COMMAND("/probation_passed", "Испытательный срок пройден"),
    PROBATION_NOT_PASSED_COMMAND("/probation_not_passed", "Испытательный срок НЕ пройден"),
    NOTIFY_CARERS_COMMAND("/notify_carers", "Уведомить опекунов");

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

    public static Command findByStringCommand(String command) {
        for (Command stringCommand : values()) {
            if (stringCommand.getCommand().equals(command)) {
                return stringCommand;
            }
        }
        return null;
    }

    public static boolean commandExists(String command) {
        for (Command stringCommand : values()) {
            if (stringCommand.getCommand().equals(command)) {
                return true;
            }
        }
        return false;
    }
}
