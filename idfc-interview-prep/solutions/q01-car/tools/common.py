def cls(name, fields, methods, stereo=None, color="#E8F0FE"):
    head = (f'<TR><TD BGCOLOR="{color}"><I>&laquo;{stereo}&raquo;</I><BR/><B>{name}</B></TD></TR>' if stereo
            else f'<TR><TD BGCOLOR="{color}"><B>{name}</B></TD></TR>')
    f = '<BR ALIGN="LEFT"/>'.join(fields) + '<BR ALIGN="LEFT"/>' if fields else ' '
    m = '<BR ALIGN="LEFT"/>'.join(methods) + '<BR ALIGN="LEFT"/>' if methods else ' '
    return f'<<TABLE BORDER="0" CELLBORDER="1" CELLSPACING="0" CELLPADDING="6">{head}<TR><TD ALIGN="LEFT">{f}</TD></TR><TR><TD ALIGN="LEFT">{m}</TD></TR></TABLE>>'
def esc(s): return s.replace('<','&lt;').replace('>','&gt;')
def graph(title, nodes, edges):
    out=['digraph G {','graph [rankdir=TB, fontname="Helvetica", fontsize=16, labelloc=t, label="%s", pad=0.3, nodesep=0.6, ranksep=0.7];'%title,
         'node [shape=plaintext, fontname="Helvetica", fontsize=11];','edge [fontname="Helvetica", fontsize=10];']
    for n,(name,fields,methods,stereo,color) in nodes.items():
        out.append(f'{n} [label={cls(name,[esc(x) for x in fields],[esc(x) for x in methods],stereo,color)}];')
    for a,b,lbl,style in edges:
        out.append(f'{a} -> {b} [label="{lbl}", {style}];')
    out.append('}'); return '\n'.join(out)
HAS='arrowtail=diamond, dir=back'
USE='style=dashed, arrowhead=vee'
EXT='arrowhead=empty'
IMPL='style=dashed, arrowhead=empty'
BLUE="#E8F0FE"; GREEN="#E6F4EA"; YEL="#FEF7E0"; RED="#FCE8E6"
DIR=("Direction",["N(0,1)  E(1,0)  S(0,-1)  W(-1,0)","+ dx, dy : int"],["+ left() : Direction","+ right() : Direction"],"enum",YEL)
GRID=("Grid",["- width, height : int","- obstacles : Set<String>"],["+ Grid(width, height)","+ addObstacle(x, y)","+ isFree(x, y) : boolean"],None,GREEN)
