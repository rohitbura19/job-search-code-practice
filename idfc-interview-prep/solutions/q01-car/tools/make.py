from common import *
import subprocess
D={}
D['01-base']=graph("Q01 base: Car on an XY plane",{
 'demo':("CarDemo",[],["+ main(args)"],None,"#F1F3F4"),
 'car':("Car",["- x, y, distance : int","- dir : Direction","- grid : Grid","- blockedAt : String"],["+ Car(x, y, dir, grid)","+ run(commands)","- move(steps, sign)","+ toString() : String"],None,BLUE),
 'grid':GRID,'dir':DIR},
 [('demo','car','creates',USE),('car','grid','has-a',HAS),('car','dir','uses',USE)])
D['02-fu1-eight-directions']=graph("Follow-up 1: 8 directions (only the enum changes)",{
 'car':("Car",["(unchanged)"],["run(commands)","move(steps, sign)"],None,BLUE),
 'dir':("Direction",["N NE E SE S SW W NW","+ dx, dy : int","- COUNT = 8"],["+ left()  : 45 deg anticlockwise","+ right() : 45 deg clockwise"],"enum",YEL),
 'grid':("Grid",["(unchanged)"],["isFree(x, y)"],None,GREEN)},
 [('car','dir','uses',USE),('car','grid','has-a',HAS)])
D['03-fu2-undo']=graph("Follow-up 2: undo with the Command pattern",{
 'ctl':("Controller",["- car : Car","- history : Deque<Command>"],["+ run(commands)","+ undo() : boolean","- parse(cmd) : Command"],None,BLUE),
 'cmd':("Command",[],["+ execute(car)","+ undo(car)"],"interface",RED),
 'turn':("TurnCommand",["- left : boolean"],["+ execute(car)","+ undo(car)  (opposite turn)"],None,"#FFFFFF"),
 'move':("MoveCommand",["- steps, sign : int","- moved : int"],["+ execute(car)","+ undo(car)  (move back 'moved')"],None,"#FFFFFF"),
 'car':("Car",["- x, y, distance","- dir, grid, blockedAt"],["+ turnLeft() / turnRight()","+ move(steps, sign) : int","+ isBlocked() / clearBlocked()"],None,BLUE),
 'grid':GRID},
 [('ctl','cmd','history stack',HAS),('turn','cmd','implements',IMPL),('move','cmd','implements',IMPL),('ctl','car','has-a',HAS),('cmd','car','acts on',USE),('car','grid','has-a',HAS)])
D['04-fu3-multiple-cars']=graph("Follow-up 3: several cars share one Grid",{
 'a':("Car A",["- id","- x, y, dir"],["run(commands)"],None,BLUE),
 'b':("Car B",["- id","- x, y, dir"],["run(commands)"],None,BLUE),
 'grid':("Grid",["- obstacles : Set<String>","- occupiedBy : Map<String, String>"],["+ place(id, x, y)","+ moveCar(id, from, to)","+ isFree(x, y)","+ whoIsAt(x, y) : String"],None,GREEN)},
 [('a','grid','shared',HAS),('b','grid','shared',HAS)])
D['05-fu4-vehicle-types']=graph("Follow-up 4: vehicle types with inheritance",{
 'veh':("Vehicle",["# x, y, distance : int","# dir : Direction","# grid : Grid"],["+ run(commands)  (final)","- move(units, sign)","# name() : String  (abstract)","# cellsPerUnit() : int = 1","# canReverse() : boolean = true"],"abstract",BLUE),
 'car':("Car",[],["name() = \"Car\""],None,"#FFFFFF"),
 'bike':("Bike",[],["name() = \"Bike\"","cellsPerUnit() = 2"],None,"#FFFFFF"),
 'tank':("Tank",[],["name() = \"Tank\"","canReverse() = false"],None,"#FFFFFF"),
 'grid':GRID},
 [('car','veh','extends',EXT),('bike','veh','extends',EXT),('tank','veh','extends',EXT),('veh','grid','has-a',HAS)])
D['06-fu5-go-to-target']=graph("Follow-up 5: G x y with BFS shortest path",{
 'car':("Car",["- x, y, distance, dir","- grid : Grid"],["+ run(commands)  (adds G x y)","- goTo(tx, ty)","- move(steps, sign)"],None,BLUE),
 'pf':("PathFinder",[],["+ shortestPath(grid, sx, sy, tx, ty)"," : List<Direction> or null"],"utility",RED),
 'grid':("Grid",["bounded only"],["isFree(x, y)"],None,GREEN),'dir':DIR},
 [('car','pf','uses',USE),('pf','grid','reads',USE),('pf','dir','returns',USE),('car','grid','has-a',HAS)])
for k,v in D.items():
    open(k+'.dot','w').write(v)
    for fmt in ('svg','png'):
        subprocess.run(['dot','-T'+fmt,'-Gdpi=130' if fmt=='png' else '-Gdpi=72',k+'.dot','-o',f'{k}.{fmt}'],check=True)
print('ok')
