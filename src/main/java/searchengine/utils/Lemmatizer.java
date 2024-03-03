package searchengine.utils;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.*;

public class Lemmatizer {


    private final LuceneMorphology luceneMorphology;
    private static final String WORD_TYPE_REGEX = "\\W\\w&&[^а-яА-Я\\s]";
    private static final String[] particlesNames = new String[]{"ПРЕДЛ", "МСП", "СОЮЗ", "МЕЖД", "ЧАСТ"};

    public static Lemmatizer getInstance() throws IOException {
        LuceneMorphology morphology = new RussianLuceneMorphology();
        return new Lemmatizer(morphology);
    }

    private Lemmatizer(LuceneMorphology luceneMorphology) {
        this.luceneMorphology = luceneMorphology;
    }


    public static void main(String[] args) throws IOException {
        String text = "МАОУ СОШ № 200 Skip to Menu Skip to Content Skip to Footer> Обратная связь Вторник Янв 02 Главная Дистанционное обучение Система дистанционного обучения Дополнительное образование Расписание Изменения в расписании Школьная жизнь Олимпиады Конкурсы Школьное научное общество Школьная газета Школьное радио Лагерь Уважаемые родители! Информацию о лагере с дневным пребыванием детей, который будет организован на базе МАОУ СОШ № 200, Вы можете найти в данном разделе. Расписание Уважаемые родители и обучающиеся! С изменениями в расписании учебных занятий Вы можете ознакомиться в данном разделе. Информация публикуется ежедневно, до 17:00. Будьте внимательны! Previous Next Stop Play Лагерь Расписание Торжественный приём главы Администрации города Екатеринбурга победителей и лауреатов фестиваля \"Юные интеллектуалы Екатеринбурга\" Автор: Кириллова М.Г. 07 Мая 2018 26 апреля 2018 г. состоялся приём главы Администрации города Екатеринбурга победителей и лауреатов фестиваля \"Юные интеллектуалы Екатеринбурга\". Подробнее... Команда МАОУ СОШ № 200 - призёр VI Городского турнира юных физиков \"Положительный заряд\" Автор: Негатина В.С. 07 Мая 2018 23 апреля 2018 г. в МАОУ гимназия № 9 состоялся VI Городской турнир юных физиков \"Положительный заряд\". Подробнее... Интеллектуальный марафон в начальных классах Автор: Ласько Е.Ю., Дорохина О.Ю. (фото) 07 Мая 2018 4 мая 2018 г. в начальных классах прошёл Интеллектуальный марафон. Подробнее... 3г на кондитерской фабрике \"9 островов\" Автор: Ласько Е.Ю. 07 Мая 2018 В конце апреля 2018 г. учащиеся 3г класса побывали на кондитерской фабрике. Подробнее... VII Фестиваль естествознания \"Земля и Вселенная\" Автор: Мехаева Е.С. 06 Мая 2018 В апреле 2018 г. в Институте естественных наук и математики УрФУ состоялся ежегодный фестиваль естествознания \"Земля и Вселенная\". Подробнее... 4а: Экскурсия на Свердловскую киностудию Автор: Веселова Е.М. 06 Мая 2018 Ученики 4а класса вместе с классным руководителем Анной Владимировной Ярочкиной посетили Свердловскую киностудию. Подробнее... План мероприятий в рамках Декады, посвящённой 73-летию Победы в Великой Отечественной войне 1941-1945 годов 06 Мая 2018 NB! План мероприятий Скачать (.doc, 34,5 кб) Внимание! 04 Мая 2018 Уважаемые родители (законные представители) обучающихся 1-4 классов! Сообщаем вам, что 7 мая 2018 г. состоятся родительские собрания по следующему графику. Подробнее... 1д: Каникулы нужно проводить с пользой Автор: Постаногова Н.С. 02 Мая 2018 Каникулы нужно проводить с пользой - так решили ученики 1д класса. Подробнее... Весенняя юридическая школа в Гуманитарном университете Автор: Аликина Е.А. 28 Апреля 2018 Весенняя юридическая школа с 2013 года посвящена памяти известного российского правоведа, одного из разработчиков Конституции РФ и Гражданского кодекса РФ, заслуженного юриста Российской Федерации, нашего земляка, доктора юридических наук, профессора Сергея Сергеевича Алексеева. Подробнее... 9б в Музее истории органов безопасности Среднего Урала Автор: Пашкова Л.К. 28 Апреля 2018 27 апреля 2018 г. ученики 9б класса посетили Музей истории органов безопасности Среднего Урала. Подробнее... Участие в мероприятиях XVI Уральской горнопромышленной декады Автор: Миронова С.А. 28 Апреля 2018 Обучающиеся МАОУ СОШ № 200 приняли участие в мероприятиях XVI Уральской горнопромышленной декады. Подробнее... 6а в музее \"Россия - моя история\" Автор: Гончаренко Г.Б. 28 Апреля 2018 В последний день перед каникулами, 7 апреля 2018 г., обучающиеся 6а класса решили посетить музей \"Россия - моя история\". Подробнее... Областная олимпиада по правам человека Автор: Аликина Е.А. 28 Апреля 2018 Ежегодно весной Гуманитарный университет открывает двери для старшеклассников Свердловской области, проводит областную олимпиаду по правам человека. Подробнее... VI Международный форум в рамках проведения Всемирного дня культуры Автор: Миронова С.А. 21 Апреля 2018 С 13 по 16 апреля 2018 г. в рамках проведения Всемирного дня культуры в УрФУ проходил VI Международный форум. Подробнее... Победы в олимпиадах по математике Автор: Миронова С.А. 21 Апреля 2018 Обучающиеся 9 классов МАОУ СОШ № 200 приняли участие в Х заочной областной олимпиаде по математике. Подробнее... 10а: Посещение УрФУ Автор: Мехаева Е.С. 21 Апреля 2018 В первый день каникул ребята из 10а вместе с классным руководителем посетили Уральский гуманитарный институт УрФУ. Подробнее... Учащиеся 200 школы \"покоряли\" космос Автор: Гордеева Диана 21 Апреля 2018 7 апреля 2018 г. на базе Уральского финансово-юридического колледжа прошла квест-игра, посвящённая Всемирному дню авиации и космонавтики. Подробнее... Финал Международного конкурса - фестиваля в рамках проекта \"Урал собирает друзей\" Автор: Маколдина Н.Г. 21 Апреля 2018 С 5 по 8 апреля 2018 г. в Екатеринбурге проходил финал Международного конкурса - фестиваля в рамках проекта \"Урал собирает друзей\". Подробнее... Публичная презентация школьных исследовательских работ \"Инженер леса XXI века\" Автор: Лукманова Кира 21 Апреля 2018 4 апреля 2018 г. состоялась публичная презентация школьных исследовательских работ \"Инженер леса XXI века\" в Уральском государственном лесотехническом университете. Подробнее... Страница 8 из 62 « Первая Предыдущая 1 2 3 4 5 6 7 8 9 10 Следующая Последняя » Сведения об ОО Основные сведения Структура и органы управления ОО Документы Образование Образовательные стандарты Руководство. Педагогический (научно-педагогический) состав Материально-техническое обеспечение и оснащенность образовательного процесса Стипендии и иные виды материальной поддержки Платные образовательные услуги Финансово-хозяйственная деятельность Вакантные места для приёма (перевода) Приём в школу Родителям будущих первоклассников Родителям будущих десятиклассников Выпускнику - 2023 Школьное питание Профилактика Законы на страже интересов детей Профилактические буклеты Информация ГИБДД Частые вопросы Обращения граждан Контакты Архив 223-ФЗ Внеклассная работа Спорт Опрос Что бы Вы хотели видеть на сайте школы? Расписание занятий Контакты с администрацией Новости учебного процесса Новости общественной жизни школы Новости сферы образования Доска почёта лучших учеников Общешкольный интерактивный форум Заметки о школьных событиях Результаты успеваемости учеников Отзывы учеников и родителей Другое ОК Итоги АИС \"Образование\" Электронный дневник Электронный журнал Внимание! Изменения в расписании на 29.12.2023 — 2-4 классы — — 5-11 классы — Цветовые обозначения в расписании Аниматика Волонтёрский отряд Эхо Победной весны Социальный проект Сейчас Online Сейчас 98 гостей онлайн Министерство просвещения Российской Федерации Министерство образования и молодёжной политики Свердловской области Департамент образования администрации г. Екатеринбурга Информационно-методический центр Чкаловского района г. Екатеринбурга Институт развития образования Екатеринбургский Дом Учителя © 2024 МАОУ СОШ № 200. Все права защищены. Официальный сайт ОО Go to Top";
        Lemmatizer lemmatizer = Lemmatizer.getInstance();
        System.out.println(Arrays.toString(lemmatizer.refactorTextAndSplit(text)));
        HashMap<String, Integer> lemmasMap = lemmatizer.lemmatize(text);
        for (Map.Entry<String, Integer> entry : lemmasMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    public HashMap<String, Integer> lemmatize(String text) {
        HashMap<String, Integer> wordBaseForms = new HashMap<>();

        String[] wordsArray = refactorTextAndSplit(text);

        for (String word : wordsArray) {
            if (isCorrectWordForm(word) && !word.isBlank()) {

                List<String> wordInfo = luceneMorphology.getMorphInfo(word);
                if (belongsToParticle(wordInfo)) {
                    continue;
                }

                List<String> normalForms = luceneMorphology.getNormalForms(word);
                String normalWord = normalForms.get(0);

                if (normalWord.length() <= 2) {
                    continue;
                }

                if (wordBaseForms.containsKey(normalWord)) {
                    wordBaseForms.put(normalWord, wordBaseForms.get(normalWord) + 1);
                } else {
                    wordBaseForms.put(normalWord, 1);
                }
            }
        }
        return wordBaseForms;
    }

    public ArrayList<String> getBasicFormsFromString(String input) throws IOException {
        input = replaceAuxiliarySymbols(input);
        LuceneMorphology luceneMorphology = new RussianLuceneMorphology();
        ArrayList<String> wordsToCount = new ArrayList<>();
        String[] wordsFromInput = input.split(" ");
        for (String w : wordsFromInput) {
            String[] words2 = luceneMorphology.getMorphInfo(w).toString().split(" ");
            if(!Arrays.asList(particlesNames).contains(words2[1].replaceAll("[^а-яА-Я]", "")) && w.length() > 2) {
                int separator = luceneMorphology.getMorphInfo(w).toString().indexOf("|");
                wordsToCount.add(luceneMorphology.getMorphInfo(w).toString().substring(1,separator));
            }
        }
        return wordsToCount;
    }

    private String[] refactorTextAndSplit(String text) {
        return Jsoup.parse(text).text().toLowerCase(Locale.ROOT)
                .replaceAll("([^а-я\\s])", " ")
                .trim()
                .split("\\s+");
    }

    public String replaceAuxiliarySymbols(String text) {
        String regexToRemoveLatinsAndPunctuation = "[^а-яА-Я\s]";
        String regexToRemoveMultipleSpaces = "[\\s]{2,}";
        return text
                .toLowerCase(Locale.ROOT)
                .replaceAll(regexToRemoveLatinsAndPunctuation, " ")
                .trim()
                .replaceAll(regexToRemoveMultipleSpaces, " ");
    }

    private boolean belongsToParticle(List<String> wordBaseForms) {
        return wordBaseForms.stream().anyMatch(this::hasParticleProperty);
    }

    private boolean hasParticleProperty(String wordBase) {
        for (String property : particlesNames) {
            if (wordBase.toUpperCase().contains(property)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCorrectWordForm(String word) {
        List<String> wordInfo = luceneMorphology.getMorphInfo(word);
        for (String morphInfo : wordInfo) {
//            int separator = luceneMorphology.getMorphInfo(morphInfo).toString().toLowerCase(Locale.ROOT).indexOf("|");
//            String morph = luceneMorphology.getMorphInfo(morphInfo).toString().toLowerCase(Locale.ROOT).substring(1,separator);
            if (morphInfo.matches(WORD_TYPE_REGEX)) {
                return false;
            }
        }
        return true;
    }
}
