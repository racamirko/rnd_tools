
ArrayList thingsToDraw;
int sizeX = 1040;
int sizeY = 660;

class DisplayUnit {
  protected float endX, endY, stepX, stepY;
  public float curX, curY;
  
  protected float endSizeX, endSizeY, stepSizeX, stepSizeY;
  public float sizeX, sizeY;
  
  protected color endColor, stepColor;
  public color curColor; 
  
  public ArrayList attributes;
  
  DisplayUnit( float startX, float startY ){
    curX = startX; curY = startY;
    sizeX = 20; sizeY = 20;
    curColor = color(68,22,170);
  }
  
  void setDestinationLocation(float x1, float y1, int numOfSteps){
    endX = x1;
    endY = y1;
    stepX = (endX - curX) / numOfSteps;
    stepY = (endY - curY) / numOfSteps;
  }
  
  void setDestinationSize(float sizeX1, float sizeY1, int numOfSteps){
    endSizeX = sizeX1;
    endSizeY = sizeY1;
    stepSizeX = (endSizeX - sizeX) / numOfSteps;
    stepSizeY = (endSizeY - sizeY) / numOfSteps;
  }
  
  void setDestinationColor( color endColor1, int numOfSteps ){
    endColor = endColor1;
    stepColor = color( (red(endColor) - red(curColor))/numOfSteps,
                       (green(endColor) - green(curColor))/numOfSteps,
                       (blue(endColor) - blue(curColor))/numOfSteps );
  }
  
  void draw(){
    updateDeltas();
    
    fill(curColor);
    rect(curX,curY, sizeX, sizeY);
  }
  
  void updateDeltas(){
    // position
    if( curX != endX ){
      curX += stepX;
      if( abs(curX-endX) < 2 ){
        stepX = 0;
        curX = endX;
      } 
    }
    
    if( curY != endY ){
      curY += stepY;
      if( abs(curY-endY) < 2 ){
        stepY = 0;
        curY = endY;
      } 
    }

    // size
    if( sizeX != endSizeX ){
      sizeX += stepSizeX;
      if( abs( sizeX - endSizeX ) < 2 ){
        sizeX = endSizeX;
        stepSizeX = 0;
      }
    }
    
    if( sizeY != endSizeY ){
      sizeY += stepSizeY;
      if( abs( sizeY - endSizeY ) < 2 ){
        sizeY = endSizeY;
        stepSizeY = 0;
      }
    }
    
    // color
    if( curColor != endColor ){
      float tmpRed = red(curColor) + red(stepColor),
          tmpGreen = green(curColor) + green(stepColor),
          tmpBlue = blue(curColor) + blue(stepColor);
      curColor = color( tmpRed, tmpGreen, tmpBlue );
      float endRed = red(endColor),
          endGreen = green(endColor),
          endBlue = blue(endColor);
      float totalDiff = abs( tmpRed - endRed ) + abs( tmpGreen - endGreen ) + abs(tmpBlue - endBlue);
      if( totalDiff < 6 ){
        curColor = endColor;
        stepColor = color(0,0,0);
      }
    }
  }
  
};

void setup(){
  size(sizeX, sizeY);
  thingsToDraw = new ArrayList();
  for( int i = 0; i < 100; i++){
    thingsToDraw.add(new DisplayUnit(random(sizeX), random(sizeY)));
  }
}

void draw(){
  fill(color(0,0,0));
  rect(0,0, sizeX, sizeY);
  fill(color(124, 44, 240));
  rect(100,100,30,50);
  for( int i = 0; i < thingsToDraw.size(); i++){
    DisplayUnit d = (DisplayUnit) thingsToDraw.get(i);
    d.draw();
  }
}

void mousePressed() {
  for( int i = 0; i < thingsToDraw.size(); i++){
    DisplayUnit d = (DisplayUnit) thingsToDraw.get(i);
    switch( (int)random(4) ){
      case 0:
        d.setDestinationLocation( random(sizeX), random(sizeY), 100 );
        break;
      case 1:
        d.setDestinationSize( 10+random(50), 10+random(50), 100 );
        break;
      case 2:
        d.setDestinationLocation( random(sizeX), random(sizeY), 100 );
        d.setDestinationSize( 10+random(50), 10+random(50), 100 );
        break;
      case 3:
        d.setDestinationColor( color( 10 + random(240), 10 + random(240), 10+random(240) ), 100  );
        break;
    }
  }
}
