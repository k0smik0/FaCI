# FACRI

Facri is Facebook Cliques Real Interactions  


Facri is a Java tool allowing to analyze your facebook friendlist, and discover if people really interacts with each others, when they're in same groups - or if they are only "friends".


Be careful with that data, Eugene! They are your friends private data!

* Phase 1:
You need to download your friends (and yours) facebook posts from walls, using a patched version of fbcmd, included here

`
cd fbcmd; ./bin/facri-get_all_data.sh
`

Get out, do some shopping, it takes a while (also some hours) - if it hangs, wait 2-3 hours, or retry the next day: facebook has per hours and/or per data downloads and/or per ip filter, and it blocks next requests. When I will have time to spend here, I'll fix this (or you could...).

* Phase 2:
`cd java/facri;
ant build;
./doConsole.sh
`

If all was fine, you will get an interactive console, with some commands to build 2 different graph: "friendships" and "interactions":

* "friendships" is your facebook friendlist showed as a graph, with your friends connected to you, and your mutual friends
* "interactions" is the graph showing real interactions between your user with your friends, and between each others; interactions are public post, likes, tags, comments.. nodes and edges are builded using data you downloaded with fbcmd

Then, you could apply some interesting (and nice) algorithm to each graphs, and see the results (numerical as graphics).

Enjoy it!






ps:
for people interested to core code, hic sunt leones:

* graph generators  
[GrapherAction](https://github.com/k0smik0/FaCRI/blob/master/java/facri/src/console/net/iubris/facri/console/actions/graph/grapher/GrapherAction.java)  
[AbstractGraphstreamGraphGenerator](https://github.com/k0smik0/FaCRI/blob/master/java/facri/src/grapher/net/iubris/facri/grapher/generators/graphstream/AbstractGraphstreamGraphGenerator.java)  
[GraphstreamFriendshipsGraphGenerator](https://github.com/k0smik0/FaCRI/blob/master/java/facri/src/grapher/net/iubris/facri/grapher/generators/friendships/graphstream/GraphstreamFriendshipsGraphGenerator.java)  
[GraphstreamInteractionsGraphGenerator](https://github.com/k0smik0/FaCRI/blob/master/java/facri/src/grapher/net/iubris/facri/grapher/generators/interactions/graphstream/GraphstreamInteractionsGraphGenerator.java)

* analyzer:  
[AnalyzeAction](https://github.com/k0smik0/FaCRI/blob/master/java/facri/src/console/net/iubris/facri/console/actions/graph/analyzer/AnalyzeAction.java)  
[AbstractGraphstreamAnalyzer](https://github.com/k0smik0/FaCRI/blob/master/java/facri/src/analyzer/net/iubris/facri/grapher/analyzer/graphstream/AbstractGraphstreamAnalyzer.java)
