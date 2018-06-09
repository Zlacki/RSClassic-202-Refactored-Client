package net.swiftpk.client.gfx;

public class Menu {

    public static boolean aBoolean220 = true;

    public static int anInt221;

    public static int anInt225;

    private static final int BLUE_MODIFIER = 176;

    private static final int GREEN_MODIFIER = 114;

    private static final int RED_MODIFIER = 114;

    private final boolean[] aBooleanArray184;

    private final boolean[] aBooleanArray185;

    private final int aColor1;

    private final int aColor2;

    private final int aColor3;

    private final int aColor4;

    private final int aColor5;

    private final int aColor6;

    private final int aColor7;

    private final int aColor8;

    private final int aColor9;

    private final int aColor10;

    private final int aColor11;

    private final int aColor12;

    public final int[] anIntArray187;

    private final int[] anIntArray189;

    private final int[] anIntArray190;

    private int currentFocusHandle;

    private final Surface gameImage;

    private final int[] menuMaxTextLength;

    private int lastMouseButton;

    private final String[][] menuListText;

    public final int[] menuListTextCount;

    private final boolean[] menuIsActionable;

    private final boolean[] menuColorIsMasked;

    private int totalMenus;

    private final boolean[] menuHasAction;

    private final int[] menuHeight;

    private final String[] menuText;

    private final int[] menuTextType;

    private final int[] menuType;

    private final int[] menuWidth;

    private final int[] menuX;

    private final int[] menuY;

    private int mouseButton;

    private int mouseClicksConsecutive;
    private int mouseX;
    private int mouseY;
    private final boolean redStringColor;

    public Menu(Surface gi, int i) {
        currentFocusHandle = -1;
        redStringColor = true;
        gameImage = gi;
        menuIsActionable = new boolean[i];
        aBooleanArray184 = new boolean[i];
        aBooleanArray185 = new boolean[i];
        menuHasAction = new boolean[i];
        menuColorIsMasked = new boolean[i];
        anIntArray187 = new int[i];
        menuListTextCount = new int[i];
        anIntArray189 = new int[i];
        anIntArray190 = new int[i];
        menuX = new int[i];
        menuY = new int[i];
        menuType = new int[i];
        menuWidth = new int[i];
        menuHeight = new int[i];
        menuMaxTextLength = new int[i];
        menuTextType = new int[i];
        menuText = new String[i];
        menuListText = new String[i][];
        aColor1 = convertRGBToLongWithModifier(114, 114, 176);
        aColor2 = convertRGBToLongWithModifier(14, 14, 62);
        aColor3 = convertRGBToLongWithModifier(200, 208, 232);
        aColor4 = convertRGBToLongWithModifier(96, 129, 184);
        aColor5 = convertRGBToLongWithModifier(53, 95, 115);
        aColor6 = convertRGBToLongWithModifier(117, 142, 171);
        aColor7 = convertRGBToLongWithModifier(98, 122, 158);
        aColor8 = convertRGBToLongWithModifier(86, 100, 136);
        aColor9 = convertRGBToLongWithModifier(135, 146, 179);
        aColor10 = convertRGBToLongWithModifier(97, 112, 151);
        aColor11 = convertRGBToLongWithModifier(88, 102, 136);
        aColor12 = convertRGBToLongWithModifier(84, 93, 120);
    }

    public void addString(int i, String s, boolean flag) {
        int j = menuListTextCount[i]++;
        if(j >= menuMaxTextLength[i]) {
            j--;
            menuListTextCount[i]--;
            System.arraycopy(menuListText[i], 1, menuListText[i], 0, j);

        }
        menuListText[i][j] = s;
        if(flag)
            anIntArray187[i] = 0xf423f;
    }

    private int convertRGBToLongWithModifier(int red, int green, int blue) {
        return Surface.convertRGBToLong((RED_MODIFIER * red) / 114, (GREEN_MODIFIER * green) / 114, (BLUE_MODIFIER * blue) / 176);
    }

    public void newBox(int i, int j, int k, int l) {
        menuType[totalMenus] = 2;
        menuIsActionable[totalMenus] = true;
        menuHasAction[totalMenus] = false;
        menuX[totalMenus] = i - k / 2;
        menuY[totalMenus] = j - l / 2;
        menuWidth[totalMenus] = k;
        menuHeight[totalMenus] = l;
        totalMenus++;
    }

    public void drawMenu() {
        for(int menu = 0; menu < totalMenus; menu++)
            if(menuIsActionable[menu])
                if(menuType[menu] == 0)
                    drawTextAddHeight(menu, menuX[menu], menuY[menu], menuText[menu], menuTextType[menu]);
                else if(menuType[menu] == 1)
                    drawTextAddHeight(menu, menuX[menu] - gameImage.stringWidth(menuText[menu], menuTextType[menu]) / 2, menuY[menu], menuText[menu], menuTextType[menu]);
                else if(menuType[menu] == 2)
                    drawBox(menuX[menu], menuY[menu], menuWidth[menu], menuHeight[menu]);
                else if(menuType[menu] == 3)
                    method149(menuX[menu], menuY[menu], menuWidth[menu]);
                else if(menuType[menu] == 4)
                    method150(menu, menuX[menu], menuY[menu], menuWidth[menu], menuHeight[menu], menuTextType[menu], menuListText[menu], menuListTextCount[menu], anIntArray187[menu]);
                else if(menuType[menu] == 5 || menuType[menu] == 6)
                    method145(menu, menuX[menu], menuY[menu], menuWidth[menu], menuHeight[menu], menuText[menu], menuTextType[menu]);
                else if(menuType[menu] == 7)
                    method152(menu, menuX[menu], menuY[menu], menuTextType[menu], menuListText[menu]);
                else if(menuType[menu] == 8)
                    method153(menu, menuX[menu], menuY[menu], menuTextType[menu], menuListText[menu]);
                else if(menuType[menu] == 9)
                    drawScrollableMenu(menu, menuX[menu], menuY[menu], menuWidth[menu], menuHeight[menu], menuTextType[menu], menuListText[menu], menuListTextCount[menu], anIntArray187[menu]);
                else if(menuType[menu] == 11)
                    method147(menuX[menu], menuY[menu], menuWidth[menu], menuHeight[menu]);
                else if(menuType[menu] == 12)
                    method148(menuX[menu], menuY[menu], menuTextType[menu]);
                else if(menuType[menu] == 14)
                    method142(menu, menuX[menu], menuY[menu], menuWidth[menu], menuHeight[menu]);

        lastMouseButton = 0;
    }

    public void drawMenuListText(int menuHandle, int index, String text) {
        menuListText[menuHandle][index] = text;
        if(index + 1 > menuListTextCount[menuHandle])
            menuListTextCount[menuHandle] = index + 1;
    }

    public int drawText(int x, int y, String s, int type, boolean flag) {
        menuType[totalMenus] = 1;
        menuIsActionable[totalMenus] = true;
        menuHasAction[totalMenus] = false;
        menuTextType[totalMenus] = type;
        menuColorIsMasked[totalMenus] = flag;
        menuX[totalMenus] = x;
        menuY[totalMenus] = y;
        menuText[totalMenus] = s;
        return totalMenus++;
    }

    private void drawTextAddHeight(int menuObject, int x, int y, String text, int type) {
        int i1 = y + gameImage.stringHeight(type) / 3;
        drawTextWithMask(menuObject, x, i1, text, type);
    }

    private void drawTextWithMask(int menuObject, int x, int y, String text, int type) {
        int color;
        if(menuColorIsMasked[menuObject])
            color = 0xffffff;
        else
            color = 0;
        gameImage.drawString(text, x, y, type, color);
    }

    public int getMenuIndex(int i) {
        return anIntArray187[i];
    }

    public String getText(int i) {
        if(menuText[i] == null)
            return "null";
        else
            return menuText[i];
    }

    public boolean hasActivated(int i) {
        if(menuIsActionable[i] && menuHasAction[i]) {
            menuHasAction[i] = false;
            return true;
        } else {
            return false;
        }
    }

    public void keyDown(int key, char keyChar) {
        if(key == 0)
            return;
        if((this.currentFocusHandle != -1) && (this.menuText[this.currentFocusHandle] != null) && (this.menuIsActionable[this.currentFocusHandle])) {
            int textLength = this.menuText[this.currentFocusHandle].length();
            if((key == 8) && (textLength > 0))
                this.menuText[this.currentFocusHandle] = this.menuText[this.currentFocusHandle].substring(0, textLength - 1);
            if(((key == 10) || (key == 13)) && (textLength > 0))
                this.menuHasAction[this.currentFocusHandle] = true;
            String validCharSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"£$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
            if(textLength < this.menuMaxTextLength[this.currentFocusHandle]) {
                for(int k = 0; k < validCharSet.length(); k++)
                    if(keyChar == validCharSet.charAt(k)) {
                        int tmp153_150 = this.currentFocusHandle;
                        String[] tmp153_146 = this.menuText;
                        tmp153_146[tmp153_150] = (tmp153_146[tmp153_150] + keyChar);
                    }
            }
            if(key == 9)
                do
                    this.currentFocusHandle = ((this.currentFocusHandle + 1) % this.totalMenus);
                while((this.menuType[this.currentFocusHandle] != 5) && (this.menuType[this.currentFocusHandle] != 6));
        }
    }

    public int makeButton(int i, int j, int k, int l) {
        menuType[totalMenus] = 10;
        menuIsActionable[totalMenus] = true;
        menuHasAction[totalMenus] = false;
        menuX[totalMenus] = i - k / 2;
        menuY[totalMenus] = j - l / 2;
        menuWidth[totalMenus] = k;
        menuHeight[totalMenus] = l;
        return totalMenus++;
    }

    public int makeTextBox(int i, int j, int k, int l, int i1, int j1, boolean flag, boolean flag1) {
        menuType[totalMenus] = 6;
        menuIsActionable[totalMenus] = true;
        aBooleanArray185[totalMenus] = flag;
        menuHasAction[totalMenus] = false;
        menuTextType[totalMenus] = i1;
        menuColorIsMasked[totalMenus] = flag1;
        menuX[totalMenus] = i;
        menuY[totalMenus] = j;
        menuWidth[totalMenus] = k;
        menuHeight[totalMenus] = l;
        menuMaxTextLength[totalMenus] = j1;
        menuText[totalMenus] = "";
        return totalMenus++;
    }

    private void method142(int i, int j, int k, int l, int i1) {
        gameImage.drawBox(j, k, l, i1, 0xffffff);
        gameImage.drawLineX(j, k, l, aColor9);
        gameImage.drawLineY(j, k, i1, aColor9);
        gameImage.drawLineX(j, (k + i1) - 1, l, aColor12);
        gameImage.drawLineY((j + l) - 1, k, i1, aColor12);
        if(anIntArray189[i] == 1) {
            for(int j1 = 0; j1 < i1; j1++) {
                gameImage.drawLineX(j + j1, k + j1, 1, 0);
                gameImage.drawLineX((j + l) - 1 - j1, k + j1, 1, 0);
            }

        }
    }

    private void method145(int i, int j, int k, int l, int i1, String s, int j1) {
        if(aBooleanArray185[i]) {
            int k1 = s.length();
            StringBuilder sBuilder = new StringBuilder();
            for(int i2 = 0; i2 < k1; i2++)
                sBuilder.append("X");
            s = sBuilder.toString();

        }
        if(menuType[i] == 5) {
            if(lastMouseButton == 1 && mouseX >= j && mouseY >= k - i1 / 2 && mouseX <= j + l && mouseY <= k + i1 / 2)
                currentFocusHandle = i;
        } else if(menuType[i] == 6) {
            if(lastMouseButton == 1 && mouseX >= j - l / 2 && mouseY >= k - i1 / 2 && mouseX <= j + l / 2 && mouseY <= k + i1 / 2)
                currentFocusHandle = i;
            j -= gameImage.stringWidth(s, j1) / 2;
        }
        if(currentFocusHandle == i)
            s = s + "*";
        int l1 = k + gameImage.stringHeight(j1) / 3;
        drawTextWithMask(i, j, l1, s, j1);
    }

    private void drawBox(int i, int j, int k, int l) {
        gameImage.setBounds(i, j, i + k, j + l);
        gameImage.drawGradient(i, j, k, l, aColor12, aColor9);
        if(aBoolean220) {
            for(int i1 = i - (j & 0x3f); i1 < i + k; i1 += 128) {
                for(int j1 = j - (j & 0x1f); j1 < j + l; j1 += 128)
                    gameImage.fade(i1, j1, 6 + anInt221, 128);

            }

        }
        gameImage.drawLineX(i, j, k, aColor9);
        gameImage.drawLineX(i + 1, j + 1, k - 2, aColor9);
        gameImage.drawLineX(i + 2, j + 2, k - 4, aColor10);
        gameImage.drawLineY(i, j, l, aColor9);
        gameImage.drawLineY(i + 1, j + 1, l - 2, aColor9);
        gameImage.drawLineY(i + 2, j + 2, l - 4, aColor10);
        gameImage.drawLineX(i, (j + l) - 1, k, aColor12);
        gameImage.drawLineX(i + 1, (j + l) - 2, k - 2, aColor12);
        gameImage.drawLineX(i + 2, (j + l) - 3, k - 4, aColor11);
        gameImage.drawLineY((i + k) - 1, j, l, aColor12);
        gameImage.drawLineY((i + k) - 2, j + 1, l - 2, aColor12);
        gameImage.drawLineY((i + k) - 3, j + 2, l - 4, aColor11);
        gameImage.resetDimensions();
    }

    private void method147(int i, int j, int k, int l) {
        gameImage.drawBox(i, j, k, l, 0);
        gameImage.drawBoxEdge(i, j, k, l, aColor6);
        gameImage.drawBoxEdge(i + 1, j + 1, k - 2, l - 2, aColor7);
        gameImage.drawBoxEdge(i + 2, j + 2, k - 4, l - 4, aColor8);
        gameImage.drawSprite(i, j, 2 + anInt221);
        gameImage.drawSprite((i + k) - 7, j, 3 + anInt221);
        gameImage.drawSprite(i, (j + l) - 7, 4 + anInt221);
        gameImage.drawSprite((i + k) - 7, (j + l) - 7, 5 + anInt221);
    }

    private void method148(int i, int j, int k) {
        gameImage.drawSprite(i, j, k);
    }

    private void method149(int i, int j, int k) {
        gameImage.drawLineX(i, j, k, 0);
    }

    private void method150(int i, int j, int k, int l, int i1, int j1, String as[], int k1, int l1) {
        int i2 = i1 / gameImage.stringHeight(j1);
        if(l1 > k1 - i2)
            l1 = k1 - i2;
        if(l1 < 0)
            l1 = 0;
        anIntArray187[i] = l1;
        if(i2 < k1) {
            int j2 = (j + l) - 12;
            int l2 = ((i1 - 27) * i2) / k1;
            if(l2 < 6)
                l2 = 6;
            if(mouseButton == 1 && mouseX >= j2 && mouseX <= j2 + 12) {
                if(mouseY > k && mouseY < k + 12 && l1 > 0)
                    l1--;
                if(mouseY > (k + i1) - 12 && mouseY < k + i1 && l1 < k1 - i2)
                    l1++;
                anIntArray187[i] = l1;
            }
            if(mouseButton == 1 && (mouseX >= j2 && mouseX <= j2 + 12 || mouseX >= j2 - 12 && mouseX <= j2 + 24 && aBooleanArray184[i])) {
                if(mouseY > k + 12 && mouseY < (k + i1) - 12) {
                    aBooleanArray184[i] = true;
                    int l3 = mouseY - k - 12 - l2 / 2;
                    l1 = (l3 * k1) / (i1 - 24);
                    if(l1 > k1 - i2)
                        l1 = k1 - i2;
                    if(l1 < 0)
                        l1 = 0;
                    anIntArray187[i] = l1;
                }
            } else {
                aBooleanArray184[i] = false;
            }
            int j3 = ((i1 - 27 - l2) * l1) / (k1 - i2);
            method151(j, k, l, i1, j3, l2);
        }
        int k2 = i1 - i2 * gameImage.stringHeight(j1);
        int i3 = k + (gameImage.stringHeight(j1) * 5) / 6 + k2 / 2;
        for(int k3 = l1; k3 < k1; k3++) {
            drawTextWithMask(i, j + 2, i3, as[k3], j1);
            i3 += gameImage.stringHeight(j1) - anInt225;
            if(i3 >= k + i1)
                return;
        }

    }

    private void method151(int i, int j, int k, int l, int i1, int j1) {
        int k1 = (i + k) - 12;
        gameImage.drawBoxEdge(k1, j, 12, l, 0);
        gameImage.drawSprite(k1 + 1, j + 1, anInt221);
        gameImage.drawSprite(k1 + 1, (j + l) - 12, 1 + anInt221);
        gameImage.drawLineX(k1, j + 13, 12, 0);
        gameImage.drawLineX(k1, (j + l) - 13, 12, 0);
        gameImage.drawGradient(k1 + 1, j + 14, 11, l - 27, aColor1, aColor2);
        gameImage.drawBox(k1 + 3, i1 + j + 14, 7, j1, aColor4);
        gameImage.drawLineY(k1 + 2, i1 + j + 14, j1, aColor3);
        gameImage.drawLineY(k1 + 2 + 8, i1 + j + 14, j1, aColor5);
    }

    private void method152(int i, int j, int k, int l, String as[]) {
        int i1 = 0;
        int j1 = as.length;
        for(int k1 = 0; k1 < j1; k1++) {
            i1 += gameImage.stringWidth(as[k1], l);
            if(k1 < j1 - 1)
                i1 += gameImage.stringWidth("  ", l);
        }

        int l1 = j - i1 / 2;
        int i2 = k + gameImage.stringHeight(l) / 3;
        for(int j2 = 0; j2 < j1; j2++) {
            int k2;
            if(menuColorIsMasked[i])
                k2 = 0xffffff;
            else
                k2 = 0;
            if(mouseX >= l1 && mouseX <= l1 + gameImage.stringWidth(as[j2], l) && mouseY <= i2 && mouseY > i2 - gameImage.stringHeight(l)) {
                if(menuColorIsMasked[i])
                    k2 = 0x808080;
                else
                    k2 = 0xffffff;
                if(lastMouseButton == 1) {
                    anIntArray189[i] = j2;
                    menuHasAction[i] = true;
                }
            }
            if(anIntArray189[i] == j2)
                if(menuColorIsMasked[i])
                    k2 = 0xff0000;
                else
                    k2 = 0xc00000;
            gameImage.drawString(as[j2], l1, i2, l, k2);
            l1 += gameImage.stringWidth(as[j2] + "  ", l);
        }

    }

    private void method153(int i, int j, int k, int l, String as[]) {
        int i1 = as.length;
        int j1 = k - (gameImage.stringHeight(l) * (i1 - 1)) / 2;
        for(int k1 = 0; k1 < i1; k1++) {
            int l1;
            if(menuColorIsMasked[i])
                l1 = 0xffffff;
            else
                l1 = 0;
            int i2 = gameImage.stringWidth(as[k1], l);
            if(mouseX >= j - i2 / 2 && mouseX <= j + i2 / 2 && mouseY - 2 <= j1 && mouseY - 2 > j1 - gameImage.stringHeight(l)) {
                if(menuColorIsMasked[i])
                    l1 = 0x808080;
                else
                    l1 = 0xffffff;
                if(lastMouseButton == 1) {
                    anIntArray189[i] = k1;
                    menuHasAction[i] = true;
                }
            }
            if(anIntArray189[i] == k1)
                if(menuColorIsMasked[i])
                    l1 = 0xff0000;
                else
                    l1 = 0xc00000;
            gameImage.drawString(as[k1], j - i2 / 2, j1, l, l1);
            j1 += gameImage.stringHeight(l);
        }

    }

    public int method157(int i, int j, int k, int l) {
        menuType[totalMenus] = 11;
        menuIsActionable[totalMenus] = true;
        menuHasAction[totalMenus] = false;
        menuX[totalMenus] = i - k / 2;
        menuY[totalMenus] = j - l / 2;
        menuWidth[totalMenus] = k;
        menuHeight[totalMenus] = l;
        return totalMenus++;
    }

    public int method158(int i, int j, int k) {
        int l = gameImage.imageWidth[k];
        int i1 = gameImage.imageHeight[k];
        menuType[totalMenus] = 12;
        menuIsActionable[totalMenus] = true;
        menuHasAction[totalMenus] = false;
        menuX[totalMenus] = i - l / 2;
        menuY[totalMenus] = j - i1 / 2;
        menuWidth[totalMenus] = l;
        menuHeight[totalMenus] = i1;
        menuTextType[totalMenus] = k;
        return totalMenus++;
    }

    public int method159(int i, int j, int k, int l, int i1, int j1, boolean flag) {
        menuType[totalMenus] = 4;
        menuIsActionable[totalMenus] = true;
        menuHasAction[totalMenus] = false;
        menuX[totalMenus] = i;
        menuY[totalMenus] = j;
        menuWidth[totalMenus] = k;
        menuHeight[totalMenus] = l;
        menuColorIsMasked[totalMenus] = flag;
        menuTextType[totalMenus] = i1;
        menuMaxTextLength[totalMenus] = j1;
        menuListTextCount[totalMenus] = 0;
        anIntArray187[totalMenus] = 0;
        menuListText[totalMenus] = new String[j1];
        return totalMenus++;
    }

    public int addChatInput(int i, int j, int k, int l, int i1, int j1, boolean flag, boolean flag1) {
        menuType[totalMenus] = 5;
        menuIsActionable[totalMenus] = true;
        aBooleanArray185[totalMenus] = flag;
        menuHasAction[totalMenus] = false;
        menuTextType[totalMenus] = i1;
        menuColorIsMasked[totalMenus] = flag1;
        menuX[totalMenus] = i;
        menuY[totalMenus] = j;
        menuWidth[totalMenus] = k;
        menuHeight[totalMenus] = l;
        menuMaxTextLength[totalMenus] = j1;
        menuText[totalMenus] = "";
        return totalMenus++;
    }

    public int addScrollableMenu(int i, int j, int k, int l, int i1, int j1, boolean flag) {
        menuType[totalMenus] = 9;
        menuIsActionable[totalMenus] = true;
        menuHasAction[totalMenus] = false;
        menuTextType[totalMenus] = i1;
        menuColorIsMasked[totalMenus] = flag;
        menuX[totalMenus] = i;
        menuY[totalMenus] = j;
        menuWidth[totalMenus] = k;
        menuHeight[totalMenus] = l;
        menuMaxTextLength[totalMenus] = j1;
        menuListText[totalMenus] = new String[j1];
        menuListTextCount[totalMenus] = 0;
        anIntArray187[totalMenus] = 0;
        anIntArray189[totalMenus] = -1;
        anIntArray190[totalMenus] = -1;
        return totalMenus++;
    }

    public void method165(int i) {
        anIntArray187[i] = 0;
        anIntArray190[i] = -1;
    }

    public void method165(int i, int base) {
        anIntArray187[i] = base;
        anIntArray190[i] = -1;
    }

    public void method170(int i) {
        menuIsActionable[i] = true;
    }

    public void method171(int i) {
        menuIsActionable[i] = false;
    }

    public void resetListTextCount(int menuHandle) {
        menuListTextCount[menuHandle] = 0;
    }

    public void resize(int handle, int x, int y, int w, int h) {
        this.menuX[handle] = x;
        this.menuY[handle] = y;
        this.menuWidth[handle] = w;
        this.menuHeight[handle] = h;
    }

    public void scroll(int handle, int i) {
        int limit = menuListTextCount[handle] - (menuHeight[handle] / gameImage.stringHeight(menuTextType[handle]));
        int diff = Math.abs(limit - anIntArray187[handle]);
        if(i > 0)
            if(diff < i)
                anIntArray187[handle] += diff;
            else
                anIntArray187[handle] += i;
        else if(i < 0 && anIntArray187[handle] > 0)
            if(anIntArray187[handle] < -i)
                anIntArray187[handle] -= anIntArray187[handle];
            else
                anIntArray187[handle] += i;
    }

    private void drawScrollableMenu(int i, int j, int k, int l, int i1, int j1, String as[], int k1, int l1) {
        int i2 = i1 / gameImage.stringHeight(j1);
        if(i2 < k1) {
            int j2 = (j + l) - 12;
            int l2 = ((i1 - 27) * i2) / k1;
            if(l2 < 6)
                l2 = 6;
            if(mouseButton == 1 && mouseX >= j2 && mouseX <= j2 + 12) {
                if(mouseY > k && mouseY < k + 12 && l1 > 0)
                    l1--;
                if(mouseY > (k + i1) - 12 && mouseY < k + i1 && l1 < k1 - i2)
                    l1++;
                anIntArray187[i] = l1;
            }
            if(mouseButton == 1 && (mouseX >= j2 && mouseX <= j2 + 12 || mouseX >= j2 - 12 && mouseX <= j2 + 24 && aBooleanArray184[i])) {
                if(mouseY > k + 12 && mouseY < (k + i1) - 12) {
                    aBooleanArray184[i] = true;
                    int l3 = mouseY - k - 12 - l2 / 2;
                    l1 = (l3 * k1) / (i1 - 24);
                    if(l1 < 0)
                        l1 = 0;
                    if(l1 > k1 - i2)
                        l1 = k1 - i2;
                    anIntArray187[i] = l1;
                }
            } else {
                aBooleanArray184[i] = false;
            }
            int j3 = ((i1 - 27 - l2) * l1) / (k1 - i2);
            method151(j, k, l, i1, j3, l2);
        } else {
            l1 = 0;
            anIntArray187[i] = 0;
        }
        anIntArray190[i] = -1;
        int k2 = i1 - i2 * gameImage.stringHeight(j1);
        int i3 = k + (gameImage.stringHeight(j1) * 5) / 6 + k2 / 2;
        for(int k3 = l1; k3 < k1; k3++) {
            int i4;
            if(menuColorIsMasked[i])
                i4 = 0xffffff;
            else
                i4 = 0;
            if(mouseX >= j + 2 && mouseX <= j + 2 + gameImage.stringWidth(as[k3], j1) && mouseY - 2 <= i3 && mouseY - 2 > i3 - gameImage.stringHeight(j1)) {
                if(menuColorIsMasked[i])
                    i4 = 0x808080;
                else
                    i4 = 0xffffff;
                anIntArray190[i] = k3;
                if(lastMouseButton == 1) {
                    anIntArray189[i] = k3;
                    menuHasAction[i] = true;
                }
            }
            if(anIntArray189[i] == k3 && redStringColor)
                i4 = 0xff0000;
            gameImage.drawString(as[k3], j + 2, i3, j1, i4);
            i3 += gameImage.stringHeight(j1);
            if(i3 >= k + i1)
                return;
        }

    }

    public int selectedListIndex(int i) {
        return anIntArray190[i];
    }

    public void setFocus(int i) {
        currentFocusHandle = i;
    }

    public boolean updateActions(int x, int y, int lastMouseDownButton, int mouseDownButton) {
        mouseX = x;
        mouseY = y;
        boolean returnVal = false;
        mouseButton = mouseDownButton;
        if(lastMouseDownButton != 0)
            lastMouseButton = lastMouseDownButton;
        if(lastMouseDownButton == 1) {
            for(int menuHandle = 0; menuHandle < totalMenus; menuHandle++) {
                if(menuIsActionable[menuHandle] && menuType[menuHandle] == 10 && mouseX >= menuX[menuHandle] && mouseY >= menuY[menuHandle] && mouseX <= menuX[menuHandle] + menuWidth[menuHandle] && mouseY <= menuY[menuHandle] + menuHeight[menuHandle]) {
                    menuHasAction[menuHandle] = true; // if it's a button
                    // and clicked
                    returnVal = true;
                }
                if(menuIsActionable[menuHandle] && menuType[menuHandle] == 14 && mouseX >= menuX[menuHandle] && mouseY >= menuY[menuHandle] && mouseX <= menuX[menuHandle] + menuWidth[menuHandle] && mouseY <= menuY[menuHandle] + menuHeight[menuHandle])
                    anIntArray189[menuHandle] = 1 - anIntArray189[menuHandle]; // no idea what this is, there is no object of type 14
            }

        }
        if(mouseDownButton == 1)
            mouseClicksConsecutive++;
        else
            mouseClicksConsecutive = 0;
        if(lastMouseDownButton == 1 || mouseClicksConsecutive > 20) {
            for(int j1 = 0; j1 < totalMenus; j1++)
                if(menuIsActionable[j1] && menuType[j1] == 15 && mouseX >= menuX[j1] && mouseY >= menuY[j1] && mouseX <= menuX[j1] + menuWidth[j1] && mouseY <= menuY[j1] + menuHeight[j1])
                    menuHasAction[j1] = true;

            mouseClicksConsecutive -= 5;
        }
        return returnVal;
    }

    public void updateText(int i, String s) {
        menuText[i] = s;
    }

}
