<div align="center">

# Assignment 1

Giangiulli Chiara (1189567; chiara.giangiulli@studio.unibo.it)  
Shtini Dilaver (1189997; dilaver.shtini@studio.unibo.it)  
Terenzi Mirco (1193420; mirco.terenzi@studio.unibo.it)  
10 Aprile 2025

</div>

## Indice

- [Analisi del problema](#analisi-del-problema)
- [Design e Architettura](#design-e-architettura)
- [Comportamento del sistema](#comportamento-del-sistema)
- [Performance](#performance)
- [Verifica](#verifica-con-java-pathfinder)

## Analisi del problema
Il problema affrontato in questo assignment è la simulazione del comportamento collettivo di boids, entità autonome che si muovono in uno spazio bidimensionale seguendo tre regole: separazione, allineamento e coesione, come introdotto da Craig Reynolds nel 1986.

Ogni boid, ad ogni iterazione del ciclo di simulazione, deve:

- Analizzare la posizione e velocità di tutti gli altri boid che lo circondano.
- Calcolare la nuova velocità e aggiornare la propria posizione.

La programmazione concorrente permette di parallelizzare questi aggiornamenti, ma alcuni aspetti richiedono particolare attenzione:

- Le velocità e le posizioni di ogni boid sono calcolate in base a quelle dei suoi vicini, bisogna quindi gestire in modo sicuro l'accesso concorrente ai dati condivisi.
- Le fasi di calcolo e aggiornamento devono essere sincronizzate per evitare letture/scritture inconsistenti.
- Per un corretto calcolo della nuova posizione dei boid, è necessario che sia prima calcolata e aggiornata la velocità.
- L'intefaccia utente deve essere reattiva e sincronizzarsi con l'aggiornamento dei boids.


## Design e Architettura

Il progetto è stato strutturato seguendo il pattern MVC (Model-View-Controller), con una netta separazione tra logica della simulazione, interfaccia utente e controllo dell'esecuzione:

- **Model** (_BoidsModel_): contiene la logica della simulazione dei boid. Gestisce la lista dei boid, aggiorna le loro posizioni e velocità secondo le regole di comportamento (separazione, coesione, allineamento).
  Per ciascuna versione è stata poi sviluppata un’estensione del modello che implementa la logica specifica del parallelismo.
- **View** (_BoidsPanel_, _BoidsView_): componente Swing responsabile della rappresentazione grafica dei boid e dell'interazione con l'utente. Fornisce i controlli richiesti (start, stop, pause, sliders, input numerico).

- **Controller** (_BoidsController_): gestisce il ciclo di vita della simulazione e funge da ponte tra la vista e il modello. In base alla versione selezionata, avvia il motore di simulazione corretto.

Questo approccio ha permesso una gestione modulare e flessibile delle diverse versioni concorrenti della simulazione.

### Versione Multithread
In questa versione, ogni boid è stato inizialmente associato a un thread dedicato. Tuttavia, questa soluzione è stata ritenuta troppo onerosa, in quanto limitata al numero di core della CPU utilizzata, e si è passati a una suddivisione dell'insieme di boids tra un sottoinsieme di thread, uno per ciascun processore disponibile sulla Java Virtual Machine (JVM).

L'implementazione di una _Barriera_ ha permesso di gestire la sincronizzazione tra l'aggiornamento delle velocità e delle posizioni, permettendo di attendere che tutti i thread abbiano terminato di aggiornare le velocità prima di passare all'aggiornamento delle posizioni.
Per evitare, inoltre, un accesso concorrente alle risorse è stata inizialmente suddivisa la lettura e il calcolo delle velocità dal loro effettivo aggiornamento, questo per tutti i boid, grazie all'utilizzo di una barriera, per garantire modifiche consistenti ed evitare race conditions.
In particolare, per migliorare le prestazioni del sistema ed evitare eccessive attese sulle barriere, l'aggiornamento dei boid è stato suddiviso in due fasi: il calcolo della nuova velocità e della nuova posizione (il quale richiede la lettura delle medesime variabili di altri boid) e l'aggiornamento effettivo delle variabili.
Questa scelta ha permesso di rimuovere una delle barriere, separando la parte di lettura e scrittura delle variabili, e di migliorare le prestazioni del sistema, in modo da evitare conflitti tra i threads.
Successivamente, viene aggiornata la view, una volta ultimata la modifica delle posizioni da parte di tutti i thread.

Infine, attraverso l'utilizzo di un _Monitor_, implementato utilizzando meccanismi di sincronizzazione forniti dalla libreria concorrente di Java (_ReentrantLock_ e _Condition_), è stata garantita la mutua esclusione nell'aggiornamento dei pesi quando l'utente interagisce con la GUI 
e per la gestione del cambio di stato (_PAUSE_ / _RESUME_ / _RUNNING_), quando il sistema viene messo in pausa o stoppato.

### Versione Task-based
In questa versione, la concorrenza viene gestita attraverso un thread pool fisso, creato tramite _ExecutorService_. La dimensione del pool è pari al numero di core disponibili sulla macchina (più uno), per massimizzare l'utilizzo delle risorse di calcolo senza introdurre overhead non necessari.
All’avvio della simulazione, per ciascun boid viene creato un task indipendente che viene sottomesso al thread pool per l’esecuzione parallela. 
Anche in questa versione, l’architettura scelta si basa sulla divisione in due sottoinsiemi: un primo gruppo di task ha il compito di leggere e calcolare velocità e posizioni, mentre un secondo gruppo di task ha il compito di memorizzare i nuovi valori.

La sincronizzazione è stata gestita sfruttando il framework _Executor_, che permette di ottenere i risultati dei task come oggetti _Future_:
- i task sono lanciati in parallelo;
- prima di procedere con l'aggiornamento si attende il completamento del lavoro di tutti i task.

Anche in questa versione viene sfruttato il _Monitor_ per la gestione dello stato globale della simulazione.

### Versione Virtual Thread
Nella versione implementata tramite i virtual threads di Java, l'architettura scelta è molto simile alla versione multithreaded, ma ciascun thread è associato a un boid, il che consente di sfruttare i vantaggi dei virtual threads, come la leggerezza e la facilità di gestione della concorrenza.
Nella loro implementazione, i threads sono stati creati analogamente ai thread utilizzati nella prima versione del progetto, 
utilizzando due barriere per poter gestire la sincronizzazione dei boids per quanto riguarda l'aggiornamento delle loro posizioni e della velocità, dividendo l'esecuzione in una parte di scrittura e una di lettura.


## Comportamento del sistema
A description of the behaviour of the system using one or multiple Petri Nets, choosing the proper level of abstraction.

## Performance
Performance tests to analyze and discuss the performance of the programs (for each version) compared to the sequential version.
Risultati con 16 Core:
Condizioni Separation - Alignment - Coehsion impostati ad 1

_SpeedUp_
Sequenziale(42827ms)	100 boid - 1000 iteration
Sequenziale(41900ms)	1000 boid - 1000 iteration
Sequenziale(520614ms)	5000 boid - 1000 iteration

	Multithread(19304ms)	100 boid - 1000 iteration: (sequenziale/multithread)	->42827/19304 = 2,218
	Multithread(18933ms)	1000 boid - 1000 iteration: (sequenziale/multithread) 	->41900/18933 = 2,213
	Multithread(76706ms)	5000 boid - 1000 iteration: (sequenziale/multithread) 	->520614/76706= 6,787

	TaskBased(19417ms)	100 boid - 1000 iteration: (sequenziale/taskBased)	->42827/19417 = 2,205
	TaskBased(21634ms)	1000 boid - 1000 iteration: (sequenziale/taskbased) 	->41900/21634 = 1,936
	TaskBased(37369ms)	5000 boid - 1000 iteration: (sequenziale/taskbased) 	->520614/37369= 13,931

	VirtualThread(19473ms)	100 boid - 1000 iteration: (sequenziale/virtualthread)	->42827/19473 = 2,199
	VirtualThread(23141ms)	1000 boid - 1000 iteration: (sequenziale/virtualthread) ->41900/23141 = 1,810
	VirtualThread(66864ms)	5000 boid - 1000 iteration: (sequenziale/virtualthread)	->520614/66864= 7,786

_Efficiency_
E = S/N -> (S = speedup, N = number of processors(16))

	Con 100 boids:
		Multithread	: E -> 0,138
		TaskBased	: E -> 0,137
		VirtualThread	: E -> 0,137

	Con 1000 boids:
		Multithread	: E -> 0,138
		TaskBased	: E -> 0,121
		VirtualThread	: E -> 0,113

	Con 5000 boids:
		Multithread	: E -> 0,424
		TaskBased	: E -> 0,870
		VirtualThread	: E -> 0,486

Ideal efficiency is 1 = all processors are used at full capacity

## Verifica con Java Pathfinder
Il codice della versione multithreaded è stato testato utilizzando [Java PathFinder (JPF)](https://en.wikipedia.org/wiki/Java_Pathfinder), 
un framework di verifica formale per programmi Java, utilizzato per l'esplorazione degli stati del programma e la 
verifica della correttezza del codice, con l'obiettivo di individuare eventuali errori di concorrenza, deadlock o 
violazioni di proprietà. Per eseguire la verifica, è stato utilizzato un modello semplificato del progetto, al fine di 
ottimizzare i tempi di esecuzione. In particolare, è stata rimossa la parte relativa all'interfaccia grafica e il numero 
di boids è stato ridotto per evitare un numero elevato di stati da esplorare. Durante la fase di test, è stato 
analizzato un solo ciclo di simulazione, in modo da concentrarsi sulla verifica della logica di aggiornamento dei boids 
e sulla gestione della concorrenza di questi ultimi tramite gli elementi di sincronizzazione implementati nel codice, 
come le barriere e i monitor.

Di seguito viene riportato il risultato della verifica, eseguita su un sistema con 10 core:
<pre>
<b>./gradlew runAssignment01Verify</b>

<b>> Task :runAssignment01Verify</b>
[WARNING] unknown classpath element: /Users/mirco/Developer/jpf-template-project/jpf-runner/build/examples
JavaPathfinder core system v8.0 (rev 81bca21abc14f6f560610b2aed65832fbc543994) - (C) 2005-2014 United States Government. All rights reserved.


====================================================== system under test
pcd.ass01.multithreaded.MultithreadedSimulation.main()

====================================================== search started: 04/04/25, 13:52
[WARNING] orphan NativePeer method: jdk.internal.reflect.Reflection.getCallerClass(I)Ljava/lang/Class;

====================================================== results
no errors detected

====================================================== statistics
elapsed time:       00:03:29
states:             new=1344566,visited=2395785,backtracked=3740351,end=288
search:             maxDepth=198,constraints=0
choice generators:  thread=1344566 (signal=0,lock=5378,sharedRef=1238321,threadApi=2,reschedule=8944), data=0
heap:               new=980953,released=778818,maxLive=788,gcCycles=3596188
instructions:       126859809
max memory:         504MB
loaded code:        classes=134,methods=3189

====================================================== search finished: 04/04/25, 13:55

Deprecated Gradle features were used in this build, making it incompatible with Gradle 8.0.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

See <a href="https://docs.gradle.org/7.4/userguide/command_line_interface.html#sec:command_line_warnings">https://docs.gradle.org/7.4/userguide/command_line_interface.html#sec:command_line_warnings</a>

<span style="color:green">BUILD SUCCESSFUL</span> in 3m 31s
2 actionable tasks: 2 executed
</pre>
