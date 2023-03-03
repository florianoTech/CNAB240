fin=open('Retorno.txt')
fout=open('Relatório.txt','w')
L=fin.readlines()
for x in range (2,len(L)-2,2):
    fout.write(L[x][15:17] + '|' + L[x][213:223]+'\n')
fout.close()
