# image-classification
Simple Image Classifier

## Wie startet man den Test?
1. [Main](/src/de/htw/cv/mj/Main.java) ausführen
2. "ImageSet" wählen
3. "Feature Type" wählen
4. "Class Measure" wählen
5. Evtl. weitere Parameter (Interest Point Window Size, Histogramm Fächer Anzahl)
6. Training der Features starten ("Train" Button)
7. "Measure Accuracy" um den Test zu starten.

## Wie sind die Ergebnisse zu interpretieren?
Angezeigt wird der *Overall Mean Rank*, die *Overall Correct Rate* und die *Confusion Matrix* für die aktuellen Einstellungen.

## Wie kann das beste Ergebnis reproduziert werden?
Einstellungen der besten 3 Ergebnisse:

### Kleines Set ###
1. **Mean Rank:** 1.0, **Correct Rate:** 1.0, 
**Einstellungen:** Interest Points (Window size: 3), Color Histogram (16³ bins), Manhattan Distance (Linear Qualified)
2. **Mean Rank:** 1.19, **Correct Rate:** 0.98, 
**Einstellungen:** Interest Points (Window size: 1), Gradient Histogram (180 bins) > Min/Max Normalisierung, Eucledian Distance (Linear Quantified)
3. **Mean Rank:** 1.32, **Correct Rate:** 0.97, 
**Einstellungen:** Interest Points (Window size:  1), Gradient Histogram + Color Histogram (180 bins & 16³ bins) > Min/Max Norm., Eucledian Distance (Linear Quantified)

### Großes Set ###
1. **Mean Rank:** 1.83, **Correct Rate:** 0.71, 
**Einstellungen:** Interest Points (Window size: 5), Color Histogram (16³ bins), Manhatten Distance (1 vs. All)
2. **Mean Rank:** 2.10, **Correct Rate:** 0.704, 
**Einstellungen:** Interest Points (Window size: 1), Gradient Histogram + Color Histogram (180 bins & 8³ bins) > Min/Max Norm., Eucledian Distance (Linear Quantified)
3. **Mean Rank:** 1.73, **Correct Rate:** 0.699, 
**Einstellungen:** Color Histogram (16³ bins), Manhatten Distance (1 vs. All)