# MaterialUnixGraphLibrary
Graphview for Android that plots points automatically based on Unix time input.

Features:
- Support for user touch input and dialogs
- Automatic y- and x-axis lebel generation
- Draw multiple lines simultioinesly

![Screenshots](https://user-images.githubusercontent.com/25138671/27264059-4dfae056-547f-11e7-9b32-644e6c5cd613.jpg)

## Sample
<a href="https://play.google.com/store/apps/details?id=com.velli20.sample" target="_blank">
  <img alt="Get it on Google Play"
       src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" height="60"/>
</a>

## Usage

### Adding depency to your project

Add ```com.github.Velli20:MaterialUnixGraphLibrary``` to your dependencies in build.gradle

```gradle
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}
    
dependencies {
    compile 'com.github.Velli20:MaterialUnixGraphLibrary:v1.5'
}
```

Add LineGraph to your layout

```xml
<com.velli20.materialunixgraph.LineGraph
        android:id="@+id/graph"
        android:layout_width="match_parent"
        android:layout_height="200dp"/>
```

### Drawing line

You can find full sample code under /sample/ directrory.
Basic line drawing:

```java
static final int GRAPH_MAX_VERTICAL_VALUE = 120;
Random mRandom = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LineGraph graph = (LineGraph) findViewById(R.id.graph);

        long unixTimeNow = System.currentTimeMillis();
        long oneDayInMillis = 1000 * 60 * 60 * 24;
        boolean showLinePoints = true;
        
        Line line = getDummyLine(unixTimeNow, unixTimeNow+oneDayInMillis, showLinePoints);
        line.setLineColor(Color.parseColor("#00b0ff"));
        line.setFillLine(true);
        line.setFillAlpha(60); /* Set alpha of the fill color 0-255 */
        line.setLineStrokeWidth(2f); 
        
        graph.setMaxVerticalAxisValue(GRAPH_MAX_VERTICAL_VALUE);
        graph.addLine(line);
     }
        
     public Line getDummyLine(long startDateInMillis, long endDateInMillis, boolean showPoints) {
        Line line = new Line();

        /* Create y-axis points for the line */
        LinePoint point;
        for (int i = 0; i < 10; i++) {
            long x = startDateInMillis + (((endDateInMillis - startDateInMillis) / 10) * i);

            point = new LinePoint(x, mRandom.nextInt(GRAPH_MAX_VERTICAL_VALUE));
            point.setDrawPoint(showPoints);

            line.addPoint(point);
        }

        return line;
    }
```

### Callbacks

To know when the user taps the graph point or line you set

```java

final LineGraph graph = (LineGraph) findViewById(R.id.graph);
graph.setOnLinePointTouchListener(new OnLinePointTouchListener() {
            @Override
            public void onLinePointClicked(Line line, LinePoint point) {
                /* User has clicked a point on the graph. Create a dialog to show above the touched point */

                LinePointDialog dialog = new LinePointDialog(point.getX(), point.getY());
                dialog.setTitle("Pseudo-Random value");
                dialog.setTitleColor(Color.parseColor("#00b0ff"));
                dialog.setContentText(String.format(Locale.getDefault(), "%s\n%.2f €", getTimeLabel(point.getX()), point.getY()));
                dialog.setContentColor(Color.parseColor("#9e9e9e"));
                graph.drawDialog(dialog);
            }
        });
```

To disable the touch input

```java
graph.setDrawUserTouchPointEnabled(boolean enabled);
```

### Styling options

```xml
...
xmlns:graph="http://schemas.android.com/apk/res-auto"
...


<com.velli20.materialunixgraph.LineGraph
        android:id="@+id/graph"
        android:layout_width="match_parent"
        android:layout_height="300dp"
        android:padding="16dp"
        
        graph:drawTimeLabelsIn24hourMode="true"  
        graph:graphFrameColor="#e0e0e0"
        graph:verticalAxisLabelColor="#9e9e9e"
        graph:horizontalAxisLabelColor="#9e9e9e"
        graph:graphFrameStrokeWidth="1dp"
        graph:verticalAxisLabelTextSize="12sp"
        graph:horizontalAxisLabelTextSize="14sp"
        graph:maxVerticalAxisValue="120"
        graph:minVerticalAxisValue="0"
        graph:maxVerticalAxisCount="4"
        graph:verticalAxisValueLabel=" EUROS"
        graph:dialogOutlineColor="#757575"
        graph:dialogBackgroundColor="#FFFFFF"
        graph:linePointRadius="3dp"
        graph:lineTouchedPointRadius="6dp"
        graph:drawLineTouchedPointIndicatorLine="true"
        graph:lineTouchedPointColor="#00b0ff"
        graph:lineTouchedPointIndicatorLineColor="#757575"/>

```
