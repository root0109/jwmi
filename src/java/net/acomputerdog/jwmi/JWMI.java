package net.acomputerdog.jwmi;

import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import net.acomputerdog.jwmi.nat.WMIWrapper;
import net.acomputerdog.jwmi.wbem.WbemLocator;

/**
 * jWMI main class.
 *
 * Should be used via JWMI.getInstance()
 */
public class JWMI {
    private static JWMI INSTANCE;

    // private constructor, only one instance
    private JWMI() {
        // open com
        WinNT.HRESULT hresult = WMIWrapper.INSTANCE.openCOM();

        // check for errors
        if (hresult.intValue() != WMIWrapper.S_OK) {
            throw new WMIException("Unable to open COM: 0x" + Integer.toHexString(hresult.intValue()), hresult);
        }
    }

    /**
     * Creates a new instance of WbemLocator
     *
     * @return Returns a new WbemLocator
     */
    public WbemLocator createWbemLocator() {
        PointerByReference locatorRef = new PointerByReference();

        // create instance
        WinNT.HRESULT hresult = WMIWrapper.INSTANCE.createLocator(locatorRef);
        if (hresult.intValue() == WMIWrapper.S_OK) {
            return new WbemLocator(locatorRef.getValue());
        } else {
            throw new WMIException("Error creating locator: 0x" + Integer.toHexString(hresult.intValue()), hresult);
        }
    }

    /**
     * Gets or creates the shared instance of JWMI.
     *
     * A method is used instead of a static field because this way any exceptions will bubble back up
     * instead of being swallowed or thrown at odd times.
     *
     * @return Return the JWMI instance
     */
    public static JWMI getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            return (INSTANCE = new JWMI());
        }
    }
}
