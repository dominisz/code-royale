import 'dart:io';
import 'dart:math';

//constants
const int typeNoStructure = -1;
const int typeBarracks = 2;
const int ownerFriendly = 0;
const int ownerEnemy = 1;

//game variables
int numSites; //number of buildings on the map
int gold; //amount of gold
int touchedSite; //id of the side Queen is touching or -1
List<SiteLocation> siteLocations;
List<Site> sites;
List<Unit> units = [];

void initializationInput() {
  numSites = int.parse(stdin.readLineSync());
  siteLocations =
      new List.generate(numSites, (i) => new SiteLocation(i));
  sites = new List.generate(numSites, (i) => new Site(i));
  for (int i = 0; i < numSites; i++) {
    List<String> inputs = stdin.readLineSync().split(' ');
    int siteId = int.parse(inputs[0]); //always from 0 to numSites - 1 ?
    siteLocations[siteId].x = int.parse(inputs[1]);
    siteLocations[siteId].y = int.parse(inputs[2]);
    siteLocations[siteId].radius = int.parse(inputs[3]);
  }
}

void inputForOneGameTurn() {
  List<String> inputs = stdin.readLineSync().split(' ');
  gold = int.parse(inputs[0]);
  touchedSite = int.parse(inputs[1]); // -1 if none

  readSites();
  readUnits();
}

void readSites() {
  for (int i = 0; i < numSites; i++) {
    List<String> inputs = stdin.readLineSync().split(' ');
    int siteId = int.parse(inputs[0]);
    sites[siteId].ignore1 = int.parse(inputs[1]); // used in future leagues
    sites[siteId].ignore2 = int.parse(inputs[2]); // used in future leagues
    sites[siteId].structureType =
        int.parse(inputs[3]); // -1 = No structure, 2 = Barracks
    sites[siteId].owner =
        int.parse(inputs[4]); // -1 = No structure, 0 = Friendly, 1 = Enemy
    sites[siteId].param1 = int.parse(inputs[5]);
    sites[siteId].param2 = int.parse(inputs[6]);
  }
}

void showSites() {
  sites.forEach((site) {
    SiteLocation siteLocation = siteLocations[site.siteId];
    stderr.writeln("$site $siteLocation"); 
  });
}

void readUnits() {
  units.clear();
  int numUnits = int.parse(stdin.readLineSync());
  for (int i = 0; i < numUnits; i++) {
    List<String> inputs = stdin.readLineSync().split(' ');
    int x = int.parse(inputs[0]);
    int y = int.parse(inputs[1]);
    int owner = int.parse(inputs[2]);
    int unitType = int.parse(inputs[3]); // -1 = QUEEN, 0 = KNIGHT, 1 = ARCHER
    int health = int.parse(inputs[4]);
    units.add(new Unit(x, y, owner, unitType, health));
  }
}

void showUnits() {
  stderr.writeln("units");
  units.forEach((unit) {
    stderr.writeln(unit);
  });
}

void gameLoop() {
  while (true) {
    inputForOneGameTurn();
    
    showSites();
    showUnits();

    print('WAIT');
    print('TRAIN');
  }
}

void main() {
  initializationInput();
  gameLoop();
}

//other classes

class Site {
  int siteId;
  int ignore1;
  int ignore2;
  int structureType;
  int owner;
  int param1;
  int param2;

  Site(this.siteId);

  @override
  String toString() {
    return 'Site{siteId: $siteId, ignore1: $ignore1, ignore2: $ignore2, structureType: $structureType, owner: $owner, param1: $param1, param2: $param2}';
  }


}

class SiteLocation {
  int siteId;
  int x;
  int y;
  int radius;

  SiteLocation(this.siteId);

  @override
  String toString() {
    return 'SiteLocation{x: $x, y: $y, radius: $radius}';
  }


}

class Unit {
  int x;
  int y;
  int owner;
  int unitType;
  int health;
  
  Unit(this.x, this.y, this.owner, this.unitType, this.health);

  @override
  String toString() {
    return 'Unit{x: $x, y: $y, owner: $owner, unitType: $unitType, health: $health}';
  }


}
