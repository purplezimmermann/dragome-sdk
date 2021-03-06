// Generated by esidl 0.4.0.

package org.w3c.dom.events;

import org.w3c.dom.html.Window;

public interface WheelEvent extends MouseEvent
{
    // WheelEvent
    public static final int DOM_DELTA_PIXEL = 0x00;
    public static final int DOM_DELTA_LINE = 0x01;
    public static final int DOM_DELTA_PAGE = 0x02;
    public float getDeltaX();
    public float getDeltaY();
    public float getDeltaZ();
    public int getDeltaMode();
    public void initWheelEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, Window viewArg, int detailArg, int screenXArg, int screenYArg, int clientXArg, int clientYArg, short buttonArg, EventTarget relatedTargetArg, String modifiersListArg, float deltaXArg, float deltaYArg, float deltaZArg, int deltaMode);
}
