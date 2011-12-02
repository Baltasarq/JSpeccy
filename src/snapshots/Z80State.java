/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package snapshots;

import z80core.Z80.IntMode;

/**
 *
 * @author jsanchez
 */
public class Z80State {
    // Acumulador y resto de registros de 8 bits
    private int regA, regB, regC, regD, regE, regH, regL;
    // Flags sIGN, zERO, 5, hALFCARRY, 3, pARITY y ADDSUB (n), carryFlag
    private int sz5h3pnFlags;
    // Acumulador alternativo y flags -- 8 bits
    private int regAalt;
    private int flagFalt;
    // Registros alternativos
    private int regBalt, regCalt, regDalt, regEalt, regHalt, regLalt;
    // Registros de prop�sito espec�fico
    // *PC -- Program Counter -- 16 bits*
    private int regPC;
    // *IX -- Registro de �ndice -- 16 bits*
    private int regIX;
    // *IY -- Registro de �ndice -- 16 bits*
    private int regIY;
    // *SP -- Stack Pointer -- 16 bits*
    private int regSP;
    // *I -- Vector de interrupci�n -- 8 bits*
    private int regI;
    // *R -- Refresco de memoria -- 7 bits*
    private int regR;
    //Flip-flops de interrupci�n
    private boolean ffIFF1 = false;
    private boolean ffIFF2 = false;
    // EI solo habilita las interrupciones DESPUES de ejecutar la
    // siguiente instrucci�n (excepto si la siguiente instrucci�n es un EI...)
    private boolean pendingEI = false;
    // Estado de la l�nea NMI
    private boolean activeNMI = false;
    // Si est� activa la l�nea INT
    // En el 48 la l�nea INT se activa durante 32 ciclos de reloj
    // En el 128 y superiores, se activa 36 ciclos de reloj
    private boolean activeINT = false;
    // Modos de interrupci�n
    private IntMode modeINT = IntMode.IM0;
    // halted == true cuando la CPU est� ejecutando un HALT (28/03/2010)
    private boolean halted = false;
    /**
     * Registro interno que usa la CPU de la siguiente forma
     *
     * ADD HL,xx      = Valor del registro H antes de la suma
     * LD r,(IX/IY+d) = Byte superior de la suma de IX/IY+d
     * JR d           = Byte superior de la direcci�n de destino del salto
     *
     * 04/12/2008     No se vayan todav�a, a�n hay m�s. Con lo que se ha
     *                implementado hasta ahora parece que funciona. El resto de
     *                la historia est� contada en:
     *                http://zx.pk.ru/attachment.php?attachmentid=2989
     *
     * 25/09/2009     Se ha completado la emulaci�n de MEMPTR. A se�alar que
     *                no se puede comprobar si MEMPTR se ha emulado bien hasta
     *                que no se emula el comportamiento del registro en las
     *                instrucciones CPI y CPD. Sin ello, todos los tests de
     *                z80tests.tap fallar�n aunque se haya emulado bien al
     *                registro en TODAS las otras instrucciones.
     *                Shit yourself, little parrot.
     */
    private int memptr;
    
    // CPU tstates
    private int tstates;
    
    public Z80State() {
    }
    
    // Acceso a registros de 8 bits
    public final int getRegA() {
        return regA;
    }

    public final void setRegA(int value) {
        regA = value & 0xff;
    }

    public final int getRegB() {
        return regB;
    }

    public final void setRegB(int value) {
        regB = value & 0xff;
    }

    public final int getRegC() {
        return regC;
    }

    public final void setRegC(int value) {
        regC = value & 0xff;
    }

    public final int getRegD() {
        return regD;
    }

    public final void setRegD(int value) {
        regD = value & 0xff;
    }

    public final int getRegE() {
        return regE;
    }

    public final void setRegE(int value) {
        regE = value & 0xff;
    }

    public final int getRegH() {
        return regH;
    }

    public final void setRegH(int value) {
        regH = value & 0xff;
    }

    public final int getRegL() {
        return regL;
    }

    public final void setRegL(int value) {
        regL = value & 0xff;
    }

    // Acceso a registros de 16 bits
    public final int getRegAF() {
        return (regA << 8) | sz5h3pnFlags;
    }

    public final void setRegAF(int word) {
        regA = (word >>> 8) & 0xff;

        sz5h3pnFlags = word & 0xff;
    }

    public final int getRegAFalt() {
        return (regAalt << 8) | flagFalt;
    }

    public final void setRegAFalt(int word) {
        regAalt = (word >>> 8) & 0xff;
        flagFalt = word & 0xff;
    }

    public final int getRegBC() {
        return (regB << 8) | regC;
    }

    public final void setRegBC(int word) {
        regB = (word >>> 8) & 0xff;
        regC = word & 0xff;
    }

    public final int getRegBCalt() {
        return (regBalt << 8) | regCalt;
    }

    public final void setRegBCalt(int word) {
        regBalt = (word >>> 8) & 0xff;
        regCalt = word & 0xff;
    }

    public final int getRegDE() {
        return (regD << 8) | regE;
    }

    public final void setRegDE(int word) {
        regD = (word >>> 8) & 0xff;
        regE = word & 0xff;
    }

    public final int getRegDEalt() {
        return (regDalt << 8) | regEalt;
    }

    public final void setRegDEalt(int word) {
        regDalt = (word >>> 8) & 0xff;
        regEalt = word & 0xff;
    }

    public final int getRegHL() {
        return (regH << 8) | regL;
    }

    public final void setRegHL(int word) {
        regH = (word >>> 8) & 0xff;
        regL = word & 0xff;
    }

    public final int getRegHLalt() {
        return (regHalt << 8) | regLalt;
    }

    public final void setRegHLalt(int word) {
        regHalt = (word >>> 8) & 0xff;
        regLalt = word & 0xff;
    }

    // Acceso a registros de prop�sito espec�fico
    public final int getRegPC() {
        return regPC;
    }

    public final void setRegPC(int address) {
        regPC = address & 0xffff;
    }

    public final int getRegSP() {
        return regSP;
    }

    public final void setRegSP(int word) {
        regSP = word & 0xffff;
    }

    public final int getRegIX() {
        return regIX;
    }

    public final void setRegIX(int word) {
        regIX = word & 0xffff;
    }

    public final int getRegIY() {
        return regIY;
    }

    public final void setRegIY(int word) {
        regIY = word & 0xffff;
    }

    public final int getRegI() {
        return regI;
    }

    public final void setRegI(int value) {
        regI = value & 0xff;
    }

    public final int getRegR() {
        return regR;
    }

    public final void setRegR(int value) {
        regR = value & 0xff;
    }

    // Acceso al registro oculto MEMPTR
    public final int getMemPtr() {
        return memptr;
    }

    public final void setMemPtr(int word) {
        memptr = word & 0xffff;
    }
    
    // Acceso a los flip-flops de interrupci�n
    public final boolean isIFF1() {
        return ffIFF1;
    }

    public final void setIFF1(boolean state) {
        ffIFF1 = state;
    }

    public final boolean isIFF2() {
        return ffIFF2;
    }

    public final void setIFF2(boolean state) {
        ffIFF2 = state;
    }

    public final boolean isNMI() {
        return activeNMI;
    }
    
    public final void setNMI(boolean nmi) {
        activeNMI = nmi;
    }

    // La l�nea de NMI se activa por impulso, no por nivel
    public final void triggerNMI() {
        activeNMI = true;
    }

    // La l�nea INT se activa por nivel
    public final boolean isINTLine() {
        return activeINT;
    }
    
    public final void setINTLine(boolean intLine) {
        activeINT = intLine;
    }

    //Acceso al modo de interrupci�n
    public final IntMode getIM() {
        return modeINT;
    }

    public final void setIM(IntMode mode) {
        modeINT = mode;
    }

    public final boolean isHalted() {
        return halted;
    }

    public void setHalted(boolean state) {
        halted = state;
    }
    
    public final boolean isPendingEI() {
        return pendingEI;
    }
    
    public final void setPendingEI(boolean state) {
        pendingEI = state;
    }
    
    public int getTstates() {
        return tstates;
    }

    public void setTstates(int value) {
        tstates = value;
    }
}
