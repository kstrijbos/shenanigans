# SOAP
Even when most applications opt for REST API's nowadays, it can still be useful to know how SOAP API's work. Even if it's just to know what REST exactly is.

# What is SOAP?
SOAP (Simple Object Access Protocol) is a message protocol to enable distributed services to communicate. SOAP supports differents transfer protocols such HTTP and TCP. The data structure of SOAP is based on XML. Using XML makes the messages easily readable by humans and comes with the benefits of using a mature standard which is known by every developer. It also supports validation according to specified schema's. A disadvantage of using XML is the overhead in size.

As mentioned, a SOAP message is an ordinary XML document comprised of following building blocks:
- The __envelope__ element: identifies the XML document as a SOAP message
- The __header__ element (optional): can contain application specific information, such as authentication and payment options
- The __body__ element: contains request and response information 
- The __fault__ element: contains errors and status information, child of the __body__ element

![SOAP envelope](../img/soap_envelope.jpg)

A skeleton SOAP message could look like follows:

```xml
<?xml version="1.0"?>

<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope/" soap:encodingStyle="http://www.w3.org/2003/05/soap-encoding">

    <soap:Header>
        ...
    </soap:Header>

    <soap:Body>
        ...
        <soap:Fault>
            ...
        </soap:Fault>
    </soap:Body>

</soap:Envelope> 
```

## The header element
The header element contains information to use when processing the request or response. It can contain application specific information like transactions and signatures. It could look like this:

```xml
<?xml version="1.0"?>

<soap:Envelopexmlns:soap="http://www.w3.org/2003/05/soap-envelope/" soap:encodingStyle="http://www.w3.org/2003/05/soap-encoding">

    <soap:Header>
        <m:Trans xmlns:m="https://www.w3schools.com/transaction/" soap:mustUnderstand="1">
            123
        </m:Trans>
    </soap:Header>

    ...

</soap:Envelope> 
```

SOAP defines only three attributes in the default namespace: __mustUnderstand__, __actor__ and __encodingStyle__. As you can see, the m:Trans child of the header element contains the __mustUnderstand__ attribute. This attribute is used to enforce the recipient to recognize the element. In the above context, it means that the recipient receiving the SOAP message must recognize the __m:Trans__ element. 

A SOAP message may pass several endpoints, or nodes, before arriving at its final destination. However, it's possible that not all parts of the SOAP message are destined for this final endpoint. The __actor__ attribute is used to address the header element to a certain endpoint:

```xml
<?xml version="1.0"?>

<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope/" soap:encodingStyle="http://www.w3.org/2003/05/soap-encoding">

    <soap:Header>
        <m:Trans xmlns:m="https://www.w3schools.com/transaction/" soap:actor="https://www.w3schools.com/code/">
            234
        </m:Trans>
    </soap:Header>

    ...

</soap:Envelope> 
```

The __encodingStyle__ attribute is used to define the data types used in the document. This attribute may appear on any SOAP element, and it will apply to that element's contents and all child elements. Note that a SOAP message has no default encoding.

## The body element
The body element contains the actual SOAP message intended for the recipient. The body could look like the following:

```xml
<?xml version="1.0"?>

<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope/" soap:encodingStyle="http://www.w3.org/2003/05/soap-encoding">

    <soap:Body>
        <m:GetPrice xmlns:m="https://www.w3schools.com/prices">
            <m:Item>Apples</m:Item>
        </m:GetPrice>
    </soap:Body>

</soap:Envelope> 
```

The example above requests the price of apples. Note that the __m:getPrice__ and __m:Item__ elements are application specific elements and are not part of the __SOAP namespace__.

A response could look like this:

```xml
<?xml version="1.0"?>

<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope/" soap:encodingStyle="http://www.w3.org/2003/05/soap-encoding">

    <soap:Body>
        <m:GetPriceResponse xmlns:m="https://www.w3schools.com/prices">
            <m:Price>1.90</m:Price>
        </m:GetPriceResponse>
    </soap:Body>

</soap:Envelope> 
```

## The WSDL file
The WSDL (__Web Services Description Language__) file describes the exposed web services. It specifies the location and the method of the web services with the following major elements:
- Types: defines the data types used by the services, using XML Schema
- Message: defines the data elements for each operation
- PortType: describes the operations that can be performed and the messages involved
- Binding: defines the protocol and data format for each PortType

The following is an example of a skeleton WSDL file:

```xml
<definitions>

    <types>
        data type definitions
    </types>

    <message>
        definition of the data being communicated
    </message>

    <portType>
        set of operations
    </portType>

    <binding>
        protocol and data format specification
    </binding>

</definitions> 
```

### The Types element
Mosten often, a web service will have input and output types. If a web service has more than one operation, then each operation might have its own input and output type. 

![WSDL structure](../img/wsdl_structure.png)

Below you can find an example of a defined Types element:

```xml
<types>

    <xs:schema
        xmlns:xs=        "http://www.w3.org/2001/XMLSchema"
        targetNamespace= "http://jenkov.com/MyService/schema"
        xmlns:tns=       "http://jenkov.com/MyService/schema"
    >

        <!-- Input type of type 'typeLatestTutorialRequest' -->
        <xs:element name="latestTutorialRequest" type="typeLatestTutorialRequest"/>

        <!-- Definition of new data type -->
        <xs:complexType name="typeLatestTutorialRequest">
          <xs:sequence>
            <xs:element  name="date"   type="xs:date"/>
          </xs:sequence>
        </xs:complexType>

        <!-- Output type -->
        <xs:element name="latestTutorialResponse" type="xs:string"/>

        <!-- Fault type -->
        <xs:element name="invalidDateError" type="xs:string"/>

    </xs:schema>

  </types>
```

In the example, __latestTutorialRequest__ is our input type. This element is of type __typeLatestTutorialRequest__. __typeLatestTutorialRequest__ on its turn can contain a date. The __latestTutorialResponse__ element is our output type of type __String__. We also define a fault element named __invalidDateError__ of type __String__.

In XML Schema you can define all kinds of elements and types. However, only elements declared as single elements (there can be only one), and as top level elements (not nested inside other elements), can be referred to by the WSDL 2.0 interface and operation elements. In other words, you could not use the date element by itself as an input or output type of an operation, since it is declared inside the __latestTutorialRequest__. 

### The Message element
Each web service has two types of messages; __input__ and __output__ messages. The __input__ message describes the parameters for each __operation__ of the __webservice__ while the __output__ message defines the __return data__ of the operations. Each message contains one or more __part__ elements to describe every parameter or return value of the message. 

Below is an example of a message element definition:

```xml
<message name = "SayHelloRequest">
   <part name = "firstName" type = "xsd:string"/>
</message>

<message name = "SayHelloResponse">
   <part name = "greeting" type = "xsd:string"/>
</message>
```

### The portType element
The __portType__ element combines multiple __message__ elements to form a complete __one-way__ or __round-trip__ operation. For example, a portType could combine a __request__ and a __response__ message into a single request/response operation:

```xml
<portType name = "Hello_PortType">
   <operation name = "sayHello">
      <input message = "tns:SayHelloRequest"/>
      <output message = "tns:SayHelloResponse"/>
   </operation>
</portType>
```

We can define four basic types of operations:
- One-way: define messages to be received without giving a response
```xml
<wsdl:definitions> 
   <wsdl:portType>
      <wsdl:operation name = "nmtoken">
         <wsdl:input name = "nmtoken"? message = "qname"/>
      </wsdl:operation>
   </wsdl:portType>
</wsdl:definitions>
```
- Request-response: the service receives a message and sends a response
```xml
<wsdl:definitions>
   <wsdl:portType>
      <wsdl:operation name = "nmtoken" parameterOrder = "nmtokens">
         <wsdl:input name = "nmtoken"? message = "qname"/>
         <wsdl:output name = "nmtoken"? message = "qname"/>
         <wsdl:fault name = "nmtoken" message = "qname"/>
      </wsdl:operation>
   </wsdl:portType>
</wsdl:definitions>
```
- Sollicit-response: the service sends a message and receives a response
```xml
<wsdl:definitions>
   <wsdl:portType> 
      <wsdl:operation name = "nmtoken" parameterOrder = "nmtokens">
         <wsdl:output name = "nmtoken"? message = "qname"/>
         <wsdl:input name = "nmtoken"? message = "qname"/>
         <wsdl:fault name = "nmtoken" message = "qname"/>
      </wsdl:operation>
   </wsdl:portType>
</wsdl:definitions>
```
- Notification: the service sends a message without expecting a response
```xml
<wsdl:definitions .... >
   <wsdl:portType .... > *
      <wsdl:operation name = "nmtoken">
         <wsdl:output name = "nmtoken"? message = "qname"/>
      </wsdl:operation>
   </wsdl:portType>
</wsdl:definitions>
```

### The Bindings element
The SOAP specification defines the structure of the SOAP messages, not how they are exchanged. This is taken care of by the so-called __SOAP bindings__. A binding provides information about how a __portType__ operation will actually be transmitted and formatted. The bindings can map the operations to several transfer protocols such as HTTP GET, HTTP POST, TCP, FTP, ... A binding can either be a __RPC__ (Remote Procedure Call) style binding or a __document__ style binding. A binding can also have an __encoded__ or a __literal__ use. This gives us four types of bindings:
- RPC encoded
- Document encoded
- RPC literal
- Document literal

An example binding could look like this:

```xml
<binding name = "Hello_Binding" type = "tns:Hello_PortType">
   <soap:binding style = "rpc" transport = "http://schemas.xmlsoap.org/soap/http"/>
   <operation name = "sayHello">
      <soap:operation soapAction = "sayHello"/>
			
      <input>
         <soap:body
            encodingStyle = "http://schemas.xmlsoap.org/soap/encoding/"
            namespace = "urn:examples:helloservice" use = "encoded"/>
      </input>
			
      <output>
         <soap:body
            encodingStyle = "http://schemas.xmlsoap.org/soap/encoding/"
            namespace = "urn:examples:helloservice" use = "encoded"/>
      </output>
   </operation>
</binding>
```

As you can see, our __binding__ element has an attribute named __type__. This type refers to the portType definition which we bind to a __transfer definition__ in our binding element. Our __soap:binding__ element specifies that the operation will be made available via SOAP. The __style__ attribute defines the style of our SOAP message format, which is __rpc__ in this case. The __transport__ attribute specifies the operation to be transmitted over HTTP. The __operation__ element binds the specific operation of our previously mapped __portType__ to our binding. 

#### RPC vs Document

#### Literal vs Encoded

## JAX-WS