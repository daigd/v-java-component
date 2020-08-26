package com.v.component.rule.engine.groovy.demo

import org.yaml.snakeyaml.Yaml

class MemoDslTest extends GroovyTestCase {

    void testDslUsage_outputXml() {
        MemoDsl.make {
            to "Nirav Assar"
            from "Barack Obama"
            body "How are things? We are doing well. Take care"
            idea "The economy is key"
            request "Please vote for me"
            xml
        }
    }

    void testDslUsage_outputHtml() {
        MemoDsl.make {
            to "Nirav Assar"
            from "Barack Obama"
            body "How are things? We are doing well. Take care"
            idea "The economy is key"
            request "Please vote for me"
            html
        }
    }

    void testDslUsage_outputText() {
        MemoDsl.make {
            to "Nirav Assar"
            from "Barack Obama"
            body "How are things? We are doing well. Take care"
            idea "The economy is key"
            request "Please vote for me"
            text
        }
    }

    void testDslUsage_outputJson() {
        MemoDsl.make {
            to "Nirav Assar"
            from "Barack Obama"
            body "How are things? We are doing well. Take care"
            idea "The economy is key"
            request "Please vote for me"
            json
        }
    }
    
    void  testDslUsage_script(){
        Yaml parser = new Yaml()
        Object example = parser.load(("src/test/resources/adult-rule.yml" as File).text)

        println(example)
    }

    void testYamLList() {
        def text = """

---
Time: 2001-11-23 15:01:42 -5
User: ed
Warning:
  Text: This is an error message for the log file
  Size: 12
  Desc: DAB
---
Time: 2001-11-23 15:02:31 -5
User: ed
Warning:
  A slightly different error
  message.
---
Date: 2001-11-23 15:03:17 -5
User: ed
Fatal:
  Unknown variable "bar"
Stack:
  - file: TopClass.py
    line: 23
    code: |
      x = MoreObject("345\\n")
  - file: MoreClass.py
    line: 58
    code: |-
      foo = bar
"""
        Yaml parser = new Yaml()
        def list = parser.loadAll(text)
        list.each { println it }
        
    }
}