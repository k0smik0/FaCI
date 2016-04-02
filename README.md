# FACI

Faci is Facebook Cliques Interactions  


Faci is a Java tool allowing to analyze your facebook friendlist, and discover if people really interacts with each others, while theyare in same groups - or if they are only "friends".


Be careful with that data, Eugene! They are your friends private data!

* Phase 1:
You need to download your friends (and yours) facebook posts from walls, using Retriever (a patched version of fbcmd, included inside)

`
cd Retriever; ./bin/facri-get_all_data.sh
`

Get out, do some shopping, it takes a while (also some hours) - if it hangs, wait 2-3 hours, or retry the next day: facebook has per-hours and/or per-data downloads and/or per-ip filters, and it blocks next requests. When I will have time to spend here, I'll fix this (or you could...).

* Phase 2:
`cd Analyzer;
ant build;
./run.sh
`

If all was fine, you will get an interactive console, with some commands to build 2 different graph: "friendships" and "interactions":

* "friendships" is your facebook friendlist showed as a graph, with your friends connected to you, and to your mutual friends
* "interactions" is the graph showing real interactions between your user with your friends, and between each others; interactions are public posts, likes, tags, comments.. nodes and edges are builded using data that you downloaded with Retriever

Then, you could apply some interesting (and nice) algorithm to each graphs, and see the results (numerical as graphical).  
  
The interactive console includes an help, of course ;)

Enjoy it!


---
some interesting numbers:
* corpus data used are ~60000 json objects (~400 people [my friends] * 150 own posts): processing this corpus (= translate json structure to java hierarchy) takes ~9sec first time; then, it uses a key-value db (Berkeley) as a cache, and this phase reduces to <3sec
* graphs generation is also reasonable fast, taking ~2sec each graph: it is not only the graph file (graphml) creation, but also the gui parts (view frame and animations - and opengl capable hardware is recommended)

-----

ps:
a presentation (in italian) is available [here](../report.pdf) : it describes the analysis using data from my facebook, of course (;D); also, in italian, you could easily notice figures showing generated graphs and histograms produced by algorithms outputs [here](../images/); finally, a movie with some demo is available [here](https://www.youtube.com/watch?v=YdH9RCgZdH8)

ps (2):
for people interested to core code, hic sunt leones:

* graph generators  
[GrapherAction](https://github.com/k0smik0/FaCI/blob/master/java/faci/src/console/net/iubris/facri/console/actions/graph/grapher/GrapherAction.java)  
[AbstractGraphstreamGraphGenerator](https://github.com/k0smik0/FaCI/blob/master/java/faci/src/grapher/net/iubris/facri/grapher/generators/graphstream/AbstractGraphstreamGraphGenerator.java)  
[GraphstreamFriendshipsGraphGenerator](https://github.com/k0smik0/FaCI/blob/master/java/faci/src/grapher/net/iubris/facri/grapher/generators/friendships/graphstream/GraphstreamFriendshipsGraphGenerator.java)  
[GraphstreamInteractionsGraphGenerator](https://github.com/k0smik0/FaCI/blob/master/java/faci/src/grapher/net/iubris/facri/grapher/generators/interactions/graphstream/GraphstreamInteractionsGraphGenerator.java)

* analyzer:  
[AnalyzeAction](https://github.com/k0smik0/FaCI/blob/master/java/faci/src/console/net/iubris/facri/console/actions/graph/analyzer/AnalyzeAction.java)  
[AbstractGraphstreamAnalyzer](https://github.com/k0smik0/FaCI/blob/master/java/faci/src/analyzer/net/iubris/facri/grapher/analyzer/graphstream/AbstractGraphstreamAnalyzer.java)
