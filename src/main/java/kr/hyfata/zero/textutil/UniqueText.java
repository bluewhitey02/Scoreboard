package kr.hyfata.zero.textutil;

public class UniqueText {
    public String makeBoldtext(String text) {
        text = text.replace("1", "\uE024");
        text = text.replace("2", "\uE025");
        text = text.replace("3", "\uE026");
        text = text.replace("4", "\uE027");
        text = text.replace("5", "\uE028");
        text = text.replace("6", "\uE029");
        text = text.replace("7", "\uE030");
        text = text.replace("8", "\uE031");
        text = text.replace("9", "\uE032");
        text = text.replace("0", "\uE033");
        return text;
    }
}