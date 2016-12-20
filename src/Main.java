
import com.project.logger.Logs;
import com.project.ui.MainPanel;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author RavirajS
 */
public class Main extends JFrame {

    public Main(String title) {
        super(title);
        Container cont = getContentPane();
        cont.add(new MainPanel());
        setContentPane(cont);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(765, 520));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            //UIManager.setLookAndFeel("com.easynth.lookandfeel.EaSynthLookAndFeel");
            //-FAILED-//UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
            //-FAILED-//UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");                                    
            //UIManager.setLookAndFeel("com.incors.plaf.kunststoff.KunststoffLookAndFeel");
            //UIManager.setLookAndFeel("net.sourceforge.napkinlaf.NapkinLookAndFeel");
            //UIManager.setLookAndFeel("com.alee.laf.WebLookAndFeel");
            //UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceBusinessLookAndFeel");

            //UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
            //UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
            //UIManager.setLookAndFeel("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");
            //UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
            //UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
            //UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
            //UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel");
            //UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
            //UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
            //UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
            UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");

            //UIManager.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");
            //UIManager.setLookAndFeel("net.infonode.gui.laf.InfoNodeLookAndFeel");

            Main m = new Main("D-VUS 2.1.0");
            m.setLocationRelativeTo(null);
            m.setVisible(true);
            m.setResizable(false);
        } catch (ClassNotFoundException cnfe) {
            Logs.write("Driver not found", cnfe);
        } catch (InstantiationException ie) {
            Logs.write("Instantiation Exception", ie);
        } catch (IllegalAccessException iae) {
            Logs.write("Illegal Access Exception", iae);
        } catch (UnsupportedLookAndFeelException ule) {
            Logs.write("Unsupported LAF Exception", ule);
        }
    }
}
