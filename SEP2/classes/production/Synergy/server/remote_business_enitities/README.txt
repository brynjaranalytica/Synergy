The package "remote_business_entities" contains a number of class, each representing a business entity. Each 
business entity implements a speicific remote interface, which can be found in 'shared.remote_business_interfaces'.
We decided to have a remote represnatation of each business entity in order to get the full potential of java RMI.
In other words, we wanted to make client-server communication some kind transparent. Every change a user is making 
(e.g. Created new projects, sends new message) is being handled through instances of remote business entities. Therefore,
we avoid implementing complex client-server protocols.