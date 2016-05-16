package si.nejcj.goalball.scoresheet.util.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * A panel for displaying html content, wrapping it in a ScrollPane, to make it
 * easier to read.
 * 
 * @author Nejc Jakic
 * 
 */
@SuppressWarnings("serial")
public class HtmlPanel extends JPanel {
  private JScrollPane m_mainScrollPane;
  private JEditorPane m_mainEditorPane;
  private URL m_urlPath;

  /**
   * Initiates the HtmlPanel.
   * 
   * @param url
   *          Url to the content which should be displayed. This content should
   *          be on the classpath of the program.
   * @param width
   *          Preferred width of this panel.
   * @param height
   *          Preferred height of this panel.
   */
  public HtmlPanel(String url, int width, int height) {
    super();
    ClassLoader cl = this.getClass().getClassLoader();
    m_urlPath = cl.getResource(url);

    initGUI(width, height);
  }

  private void initGUI(int width, int height) {
    try {
      Dimension size = new Dimension(width, height);

      setLayout(new BorderLayout());
      {
        m_mainScrollPane = new JScrollPane();
        add(m_mainScrollPane);
        m_mainEditorPane = new JEditorPane();
        m_mainScrollPane.setViewportView(m_mainEditorPane);
        m_mainEditorPane.setBackground(new Color(232, 232, 232));
        m_mainEditorPane.setContentType("text/html");
        m_mainEditorPane.setPage(m_urlPath);
        m_mainEditorPane.setEditable(false);
        m_mainEditorPane.addHyperlinkListener(new HyperlinkListener() {
          public void hyperlinkUpdate(HyperlinkEvent event) {
            if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
              try {
                m_mainEditorPane.setPage(event.getURL());
              } catch (IOException ioe) {
                // No action necessary
                // Link will not be followed
              }
            }
          }
        });
        m_mainScrollPane.setSize(size);
        this.setSize(size);
        this.setPreferredSize(size);
      }

    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, e.getMessage());
    }
  }
}
