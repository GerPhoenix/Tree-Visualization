# Das VisualizableNode Interface
Wenn sie einen Baum mit dem TreeVisualizer zeichnen wollen, müssen die Baumknoten das VisualizableNode Interface implementieren.
Um den Baum dann zu zeichnen erstellen sie ein TreeVisualizer Objekt. 

# Der Treevisualizer
Der TreeVisualizer kann mit seinem Standard Konstruktor initalisiert werden um Bäume mit beliebiger Anzahl an Schlüsseln und Kinderknoten zu visualisieren.
Komplexere Konstruktoren mit denen zum Beispiel Schriftgröße und Knotenfarbe eingestellt werden können sind auch vorhanden.
Sehen sie sich das JavaDoc des TreeVisualizers an wenn sie mehr Informationen bezüglich seiner Konstrukoren und deren Parameter erhalten möchten.
Alle Attribute die über den Konstruktor gesetzt wurden können auch nachträglich über die entsprechenden Getter und Setter gelesen und verändert werden.

# Zeichnen
Um Bäume zu zeichnen rufen sie die `draw(VisualizableNode root)` Methode auf ihrem TreeVisualizer Objekt auf. 
Ein Fenster sollte sich nun öffnen in dem sie den gezeichneten Baum sehen sollten.
Um weitere Bäume zu zeichen rufen sie die Methode einfach erneut auf und der neue Baum sollte im Fenster gezeichnet werden.
Fensterfunktionen:
  1. Durch `scrollen` im Fenster zoomen.
  2. Durch `draggen` mit `Rechtsklick` oder auf leeren Stellen auch mit `Linksklick`, können sie das Sichtfeld verschieben.
  3. Einzelne Knoten und die Verbindungen zu anderen Knoten mit `Linksklick` markieren.
  4. Mehrere Knoten durch halten von `SHIFT` oder `STRG` und `Linksklick` markieren.
  5. Einzelne Knoten durch `gedrückt halten` von `Linksklick´ auf dem Knoten verschieben.
  
# Anmerkungen
Machen sie die Bäume nich zu groß da die Oberfläche sonst unübersichtlich wird und die Reaktivität der Fensterfunktionen beeinträchtigt wird.
Grundsetzlich empfehlen wir die Knotenmenge unter 300 zu halten um eine gute Reaktivität und Übersicht zu gewährleisten. 
Sollte es nötig sein wären jedoch auch Mengen von mehreren Tausend Knoten möglich, sie sollten dann jedoch nicht zu Weit herauszoomen um die angezeigte Knotenmenge gering zu halten.
  

