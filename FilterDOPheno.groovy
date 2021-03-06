// this computes the geometric mean of the ranks based on individual scores and filters by cutoff


def cutoff = new Double(args[1])
def mincooc = 0

def fout = new PrintWriter(new BufferedWriter(new FileWriter("filtered-doid-pheno-" + args[1] + ".txt")))

def l = []
def doid = null
def name1 = ""
def name2 = ""
new File(args[0]).splitEachLine("\t") { line ->
  def id = line[0]
  if (doid == null) {
    doid = id
  }
  def mp = line[1]
  def oldname1 = name1
  def oldname2 = name2
  name1 = line[10]
  name2 = line[11]
  if (id != doid) {
    println "Processing $doid..."
    //    def lts = l.sort { it.tscore }.reverse() //.indexOf(exp) / lsize
    l = l.sort { it.pmi }.reverse() //.indexOf(exp) / lsize
    //    def lzs = l.sort { it.zscore }.reverse() //.indexOf(exp) / lsize
    //    def llmi = l.sort { it.lmi }.reverse() //.indexOf(exp) / lsize
    //    def llgl = l.sort { it.lgl } //.indexOf(exp) / lsize
    l.removeAll { it.pmi == Double.NaN }
    def lsize = l.size()
    
    for (int i = 0 ; i < cutoff && i < lsize ; i++) {
      //      lts[i].score1 = i/lsize
      //      lpmi[i].score = i/lsize
      fout.println("$doid\t${l[i].mp}\t${l[i].tscore}\t${l[i].zscore}\t${l[i].lmi}\t${l[i].pmi}\t${l[i].lgl}\t0\t$oldname1\t${l[i].name}")
      //      lzs[i].score3 = i/lsize
      //      llmi[i].score4 = i/lsize
      //      llgl[i].score5 = i/lsize
    }
    doid = id
    l = []
  }
  try {
    Expando exp = new Expando()
    exp.mp = mp
    exp.name = line[11]
    exp.tscore = new Double(line[2])
    exp.zscore = new Double(line[3])
    exp.lmi = new Double(line[4])
    exp.pmi = new Double(line[5])
    exp.lgl = new Double(line[6])
    exp.cooc = new Integer(line[7])
    l << exp
  } catch (Exception E) {
    println E.getMessage()
  }
}
fout.flush()
fout.close()
