# MaterialUnixGraphLibrary
Graphview for Android that plots points automatically based on Unix time input.

Features:
- Support for user touch input and dialogs
- Automatic y- and x-axis lebel generation
- Draw multiple lines simultioinesly

![Screenshots](https://user-images.githubusercontent.com/25138671/27264059-4dfae056-547f-11e7-9b32-644e6c5cd613.jpg)


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
