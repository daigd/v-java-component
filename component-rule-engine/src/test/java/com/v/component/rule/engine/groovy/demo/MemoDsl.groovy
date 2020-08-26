package com.v.component.rule.engine.groovy.demo

import groovy.json.JsonBuilder
import groovy.xml.MarkupBuilder

// 文章：
// http://assarconsulting.blogspot.com/2012/01/groovy-dsl-simple-example.html
class MemoDsl{
    String toText
    String fromText
    String body
    def sections = []

    /**
     * This method accepts a closure which is essentially the DSL. Delegate the 
     * closure methods to
     * the DSL class so the calls can be processed
     */
    def static make(closure) {
        MemoDsl memoDsl = new MemoDsl()
        // any method called in closure will be delegated to the memoDsl class
        closure.delegate = memoDsl
        closure()
    }

    /**
     * Store the parameter as a variable and use it later to output a memo
     */
    def to(String toText) {
        this.toText = toText
    }

    def from(String fromText) {
        this.fromText = fromText
    }

    def body(String bodyText) {
        this.body = bodyText
    }

    /**
     * When a method is not recognized, assume it is a title for a new section. Create a simple
     * object that contains the method name and the parameter which is the body.
     */
    def methodMissing(String methodName, args) {
        def section = new Section(title: methodName, body: args[0])
        sections << section
    }

    /**
     * 'get' methods get called from the dsl by convention. Due to groovy closure delegation,
     * we had to place MarkUpBuilder and StringWrite code in a static method as the delegate of the closure
     * did not have access to the system.out
     */
    def getXml() {
        doXml(this)
    }

    def getHtml() {
        doHtml(this)
    }

    def getText() {
        doText(this)
    }

    def getJson() {
        doJson(this)
    }

    /**
     * Use markupBuilder to create a customer xml output
     */
    private static doXml(MemoDsl memoDsl) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.memo() {
            to(memoDsl.toText)
            from(memoDsl.fromText)
            body(memoDsl.body)
            // cycle through the stored section objects to create an xml tag
            for (s in memoDsl.sections) {
                "$s.title"(s.body)
            }
        }
        println writer
    }

    /**
     * Use markupBuilder to create an html xml output
     */
    private static doHtml(MemoDsl memoDsl) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.html() {
            head {
                title("Memo")
            }
            body {
                h1("Memo")
                h3("To: ${memoDsl.toText}")
                h3("From: ${memoDsl.fromText}")
                p(memoDsl.body)
                // cycle through the stored section objects and create uppercase/bold section with body
                for (s in memoDsl.sections) {
                    p {
                        b(s.title.toUpperCase())
                    }
                    p(s.body)
                }
            }
        }
        println writer
    }

    /**
     * Use markupBuilder to create an html xml output
     */
    private static doText(MemoDsl memoDsl) {
        String template = "Memo\nTo: ${memoDsl.toText}\nFrom: ${memoDsl.fromText}\n${memoDsl.body}\n"
        def sectionStrings =""
        for (s in memoDsl.sections) {
            sectionStrings += s.title.toUpperCase() + "\n" + s.body + "\n"
        }
        template += sectionStrings
        println template
    }

    private static doJson(MemoDsl memoDsl) {

//        def generator = new JsonGenerator.Options()
//                .excludeNulls()
//                .excludeFieldsByName('make', 'country', 'record')
//                .excludeFieldsByType(Number)
//                .addConverter(URL) { url -> "http://groovy-lang.org" }
//                .build()
        def builder = new JsonBuilder()
        
        builder {
            to(memoDsl.toText)
            from(memoDsl.fromText)
            body(memoDsl.body)
            for (s in memoDsl.sections) {
                "$s.title"(s.body)
            }
        }
        println builder.toPrettyString()
    }
}