package com.imjona.ctw.managers.game;

import com.imjona.ctw.CTW;
import com.imjona.ctw.enums.GameState;

public class GameEngine {
    private final CTW ctw;
    private final int countdown = 25;
    public GameState gameState;
    private final long lastSave = 0L;
    private final int mate = 40;

    public GameEngine(CTW ctw) {
        this.ctw = ctw;
    }
}
