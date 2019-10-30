import re
import json
import csv
import xml.etree.ElementTree as ET

# LIVOX question_coarse
# {ABBR,DESC,ENTY,HUM,LOC,NUM}
# Unclassified still = UNC
switcher = {
    'who': "HUM",
    'what': "UNC",
    'when': "UNC",
    'where': "LOC",
    'why': "DESC",
    'how': "DESC"
}

reg_03 = '''
    <top>

    <num> Number: (.*)

    <type> Type: (.*)

    <desc> Description:
    (.*)

    </top>
    '''

reg_02_99 = '''
<top>

<num> Number: (.*)

<desc> Description:
(.*)

</top>
'''


# 2004 Dataset
def load_from_xml():
    tree = ET.parse('QA2004_testset.xml')
    root = tree.getroot()
    questions = []
    for targets in root:
        for qa in targets:
            q = qa.find('q')
            if q.get('type') == 'FACTOID' or q.get('type') == 'LIST':
                questions.append(q.text.strip())
    return questions


# 2003, 2002 - 1999 Data
def load_from_text(reg, file):
    questions = []
    with open(file, 'r') as text:
        matches = re.findall(reg, text.read())
        for (num, question) in matches:
            questions.append(question)
    return questions


# Parse Questions by type
def clean_and_label(questions):
    clean_factoids = []
    labels = []
    with open('sentence-structure-config.json', 'r') as json_file:
        structure = json.load(json_file)
        prepositions = structure['prepositions']
        for question in questions:
            for prepos in prepositions:
                question = question.replace("?", " ")
                question = question.replace(" " + prepos + " ", " ")
            # Split the question to identify the initiation word
            words = question.lower().split()
            init = words[0].lower()
            classified = switcher.get(init, "UNC")
            # Add the label, append the cleaned questions
            labels.append(classified)
            clean_factoids.append(question)
    return clean_factoids, labels


def count_classes(questions, labels):   
    #{ABBR,DESC,ENTY,HUM,LOC,NUM}
    ABBR = []
    DESC = []
    ENTY = []
    HUM = []
    LOC = []
    NUM = []
    UNC = []
    count_switcher = {
        'ABBR': ABBR,
        'DESC': DESC,
        'ENTY': ENTY,
        'HUM': HUM,
        'LOC': LOC,
        'NUM': NUM,
    }
    for i in range(len(questions)):
        class_counter  = count_switcher.get(labels[i], UNC)
        class_counter.append(questions[i])
    print(f"{len(ABBR)}, {len(DESC)}, {len(ENTY)}, {len(HUM)}, {len(LOC)}, {len(NUM)}, {len(UNC)}") 
        

# Write all the question to a json
def write_to_csv(filename, questions, labels):
    with open(filename, 'w') as csv_file:
        writer = csv.writer(csv_file, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
        writer.writerow(['Question', 'Class'])
        for i in range(len(questions)):
            writer.writerow([questions[i], labels[i]])


# Write a main
def main():
    q_04 = load_from_xml()
    clean_04, labels = clean_and_label(q_04)
    write_to_csv("questions04.csv", q_04, labels)
    count_classes(q_04, labels)


if __name__ == '__main__':
    main()
