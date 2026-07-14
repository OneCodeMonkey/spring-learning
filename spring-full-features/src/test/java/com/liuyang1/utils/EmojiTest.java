package com.liuyang1.utils;

import com.liuyang1.impl.utils.MyStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link MyStringUtils#removeEmoji(String)} 的单元测试。
 */
public class EmojiTest {

    // ------------------------------------------------------------------
    // 边界输入：null / 空串 / 不含 emoji
    // ------------------------------------------------------------------

    @Test
    public void testNullInput() {
        assertNull(MyStringUtils.removeEmoji(null));
    }

    @Test
    public void testEmptyInput() {
        assertEquals("", MyStringUtils.removeEmoji(""));
    }

    @Test
    public void testPlainTextWithoutEmoji() {
        String text = "今天天气不错，去打篮球吧！abc123，中文标点，都保留。";
        assertEquals(text, MyStringUtils.removeEmoji(text));
    }

    @Test
    public void testTextWithNewlineAndWhitespace() {
        String text = "第一行\n第二行\t带 tab 和空格  结束";
        assertEquals(text, MyStringUtils.removeEmoji(text));
    }

    // ------------------------------------------------------------------
    // 单个 emoji：各 Unicode 区块的代表字符
    // ------------------------------------------------------------------

    @ParameterizedTest
    @CsvSource({
            "'☀', '[BLACK SUN WITH RAYS]'",             // 0x2600 区块下界
            "'➿', '[DOUBLE CURLY LOOP]'",               // 0x27BF 区块上界
            "'⬀', '[NORTH EAST WHITE ARROW]'",          // 0x2B00 区块下界
            "'⯿', '[HELLSCHREIBER PAUSE SYMBOL]'",      // 0x2BFF 区块上界
            "'🌀', '[CYCLONE]'",                         // 0x1F300 区块下界
            "'🗿', '[MOYAI]'",                           // 0x1F5FF 区块上界
            "'😀', '[GRINNING FACE]'",                   // 0x1F600 表情符号
            "'🚀', '[ROCKET]'",                          // 0x1F680 交通与地图符号
            "'🍊', '[TANGERINE]'",                       // 常见业务场景 emoji
            "'🏀', '[BASKETBALL AND HOOP]'",
            "'⭐', '[WHITE MEDIUM STAR]'",
            "'🧠', '[BRAIN]'",                            // 0x1F900 补充符号与图形
            "'🩰', '[BALLET SHOES]'",                     // 0x1FA70 符号与图形扩展-A
    })
    public void testSingleKnownEmoji(String emoji, String expected) {
        System.out.println(expected);
        System.out.println(MyStringUtils.removeEmoji(emoji));
        assertEquals(expected, MyStringUtils.removeEmoji(emoji));
    }

    @Test
    public void testEmojiSurroundedByText() {
        String actual = MyStringUtils.removeEmoji("今天天气🍊不错");
        assertEquals("今天天气[TANGERINE]不错", actual);
    }

    @Test
    public void testMultipleEmojisInOneString() {
        String actual = MyStringUtils.removeEmoji("😀🍊🏀");
        assertEquals("[GRINNING FACE][TANGERINE][BASKETBALL AND HOOP]", actual);
    }

    // ------------------------------------------------------------------
    // 区域指示符组合（国旗）：由两个 surrogate-pair 码点拼成
    // ------------------------------------------------------------------

    @Test
    public void testFlagEmojiChina() {
        // 🇨🇳 = U+1F1E8 U+1F1F3
        String actual = MyStringUtils.removeEmoji("国旗🇨🇳来了");
        assertEquals("国旗[REGIONAL INDICATOR SYMBOL LETTER C][REGIONAL INDICATOR SYMBOL LETTER N]来了", actual);
    }

    // ------------------------------------------------------------------
    // 修饰符：ZWJ / 变体选择符 / keycap / 肤色，附着在 emoji 上应被丢弃
    // ------------------------------------------------------------------

    @Test
    public void testVariationSelectorIsDropped() {
        // ❤️ = U+2764 (HEAVY BLACK HEART) + U+FE0F (VARIATION SELECTOR-16)
        String actual = MyStringUtils.removeEmoji("我❤️你");
        assertEquals("我[HEAVY BLACK HEART]你", actual);
    }

    @Test
    public void testSkinToneModifierIsDropped() {
        // 👍🏽 = U+1F44D (THUMBS UP SIGN) + U+1F3FD (EMOJI MODIFIER FITZPATRICK TYPE-4)
        String actual = MyStringUtils.removeEmoji("👍🏽");
        assertEquals("[THUMBS UP SIGN]", actual);
    }

    @Test
    public void testKeycapModifierIsDropped() {
        // 1️⃣ = '1' + U+FE0F (VARIATION SELECTOR-16) + U+20E3 (COMBINING ENCLOSING KEYCAP)
        String actual = MyStringUtils.removeEmoji("1️⃣");
        assertEquals("1", actual);
    }

    @Test
    public void testZwjJoinedFamilyEmojiIsFlattened() {
        // 👨‍👩‍👧 = MAN + ZWJ + WOMAN + ZWJ + GIRL，ZWJ 被丢弃，三个 emoji 分别转换
        String man = "👨";
        String woman = "👩";
        String girl = "👧";
        String zwj = "‍";
        String actual = MyStringUtils.removeEmoji(man + zwj + woman + zwj + girl);
        assertEquals("[MAN][WOMAN][GIRL]", actual);
    }

    // ------------------------------------------------------------------
    // 未命中 Unicode 官方名称的 emoji：走 EMOJI_<hex> 兜底分支
    // ------------------------------------------------------------------

    @Test
    public void testUnnamedEmojiFallsBackToHexCodePoint() {
        // U+1FAF0 (HAND WITH INDEX FINGER AND THUMB CROSSED) 在部分 JDK Unicode 版本中未命名
        String actual = MyStringUtils.removeEmoji("🫰");
        assertEquals("[" + "EMOJI_1FAF0" + "]", actual);
    }

    // ------------------------------------------------------------------
    // 边界值：紧邻各区块上下界之外的字符，不应被识别为 emoji
    // ------------------------------------------------------------------

    @Test
    public void testCharacterJustBelowFirstRangeIsKept() {
        // U+25FF 在 0x2600 区块之前一位，不应被转换
        String actual = MyStringUtils.removeEmoji("◿");
        assertEquals("◿", actual);
    }

    @Test
    public void testCharacterJustAboveMiscSymbolsRangeIsKept() {
        // U+27C0 在 0x27BF 区块之后一位，不应被转换
        String actual = MyStringUtils.removeEmoji("⟀");
        assertEquals("⟀", actual);
    }

    @Test
    public void testCharacterJustBelowArrowsRangeIsKept() {
        // U+2AFF 在 0x2B00 区块之前一位，不应被转换
        String actual = MyStringUtils.removeEmoji("⫿");
        assertEquals("⫿", actual);
    }

    @Test
    public void testCharacterJustAboveArrowsRangeIsKept() {
        // U+2C00 在 0x2BFF 区块之后一位，不应被转换
        String actual = MyStringUtils.removeEmoji("Ⰰ");
        assertEquals("Ⰰ", actual);
    }

    @Test
    public void testCharacterJustBelowSupplementalSymbolsRangeIsKept() {
        // U+1FB00 在 0x1FAFF 区块之后一位，不应被转换
        String actual = MyStringUtils.removeEmoji("🬀");
        assertEquals("🬀", actual);
    }

    // ------------------------------------------------------------------
    // 综合场景：中英文、标点、多个 emoji、国旗、修饰符混排
    // ------------------------------------------------------------------

    @Test
    public void testMixedRealWorldSentence() {
        String input = "今天天气🍊不错😀，去打篮球🏀吧！还有国旗🇨🇳和普通文字abc123，中文标点，都保留。";
        String expected = "今天天气[TANGERINE]不错[GRINNING FACE]，去打篮球[BASKETBALL AND HOOP]吧！"
                + "还有国旗[REGIONAL INDICATOR SYMBOL LETTER C][REGIONAL INDICATOR SYMBOL LETTER N]"
                + "和普通文字abc123，中文标点，都保留。";
        assertEquals(expected, MyStringUtils.removeEmoji(input));
    }

    @Test
    public void testLongTextWithManyRepeatedEmoji() {
        StringBuilder input = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            input.append("文本").append(i).append("🍊");
            expected.append("文本").append(i).append("[TANGERINE]");
        }
        assertEquals(expected.toString(), MyStringUtils.removeEmoji(input.toString()));
    }

    @Test
    public void testConsecutiveEmojiWithoutTextBetween() {
        String actual = MyStringUtils.removeEmoji("🍊🍊🍊");
        assertEquals("[TANGERINE][TANGERINE][TANGERINE]", actual);
    }

    @Test
    public void testEmojiAtStringBoundaries() {
        String actual = MyStringUtils.removeEmoji("🍊中间文字🍊");
        assertEquals("[TANGERINE]中间文字[TANGERINE]", actual);
    }

    // ------------------------------------------------------------------
    // 幂等性：对已经转换过的文本再执行一次，结果应保持不变
    // ------------------------------------------------------------------

    @Test
    public void testIdempotentOnAlreadyConvertedText() {
        String once = MyStringUtils.removeEmoji("开心😀");
        String twice = MyStringUtils.removeEmoji(once);
        assertEquals(once, twice);
    }
}
