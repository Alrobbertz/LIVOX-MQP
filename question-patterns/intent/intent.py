import csv
import pandas as pd
import numpy as np

import matplotlib.pyplot as plt

from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.feature_selection import chi2

from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfTransformer

from sklearn.naive_bayes import MultinomialNB
from sklearn.linear_model import LogisticRegression
from sklearn.ensemble import RandomForestClassifier
from sklearn.svm import LinearSVC

from sklearn.model_selection import cross_val_score


# Load the CSV
df = pd.read_csv('/questions/intent/Question_Classification_Dataset.csv')
df.head()


# Define the Columns
col = ['Questions', 'Category0', 'Category1', 'Category2']
df = df[col]

# Get Rid of null values
df = df[pd.notnull(df['Questions'])]
df.columns = ['Questions', 'Category0', 'Category1', 'Category2']

# Generate Int values for Class categories as new column in frame
#df['Class']= df.apply(lambda x:'%s_%s' % (x['Category1'],x['Category2']), axis=1)
df['Class'] = df['Category0']
df['category_id'] = df['Class'].factorize()[0]

# Generate some dictionaries?
category_id_df = df[['Class', 'category_id']].drop_duplicates().sort_values('category_id')
category_to_id = dict(category_id_df.values)
id_to_category = dict(category_id_df[['category_id', 'Class']].values)
df.head()

print("== Class Balances ==")
print(df.groupby('Class').Questions.count())

# Do TF-IDF Vectorization, Set features, labels
tfidf = TfidfVectorizer(sublinear_tf=True, min_df=1, norm='l2', encoding='latin-1', ngram_range=(1, 2), stop_words='english')
features = tfidf.fit_transform(df.Questions).toarray()
labels = df.category_id
print(features.shape)


# Chi^2 to find term most related with Classes
# N = 1
# for Class, category_id in sorted(category_to_id.items()):
#   features_chi2 = chi2(features, labels == category_id)
#   indices = np.argsort(features_chi2[0])
#   feature_names = np.array(tfidf.get_feature_names())[indices]
#   unigrams = [v for v in feature_names if len(v.split(' ')) == 1]
#   bigrams = [v for v in feature_names if len(v.split(' ')) == 2]
#   trigrams = [v for v in feature_names if len(v.split(' ')) == 3]
#   print("# '{}':".format(Class))
#   print("  . Most correlated unigrams:\n. {}".format('\n. '.join(unigrams[-N:])))
#   print("  . Most correlated bigrams:\n. {}".format('\n. '.join(bigrams[-N:])))
#   print("  . Most correlated trigrams:\n. {}".format('\n. '.join(trigrams[-N:])))


# Train a Naive Bayes Classifier
# X_train, X_test, y_train, y_test = train_test_split(df['Question'], df['Class'], random_state = 0)
# count_vect = CountVectorizer()
# X_train_counts = count_vect.fit_transform(X_train)
# tfidf_transformer = TfidfTransformer()
# X_train_tfidf = tfidf_transformer.fit_transform(X_train_counts)
# clf = MultinomialNB().fit(X_train_tfidf, y_train)



# Give it a Test
# print("--Testing--")
# print(clf.predict(count_vect.transform(["Who is the prime minister of Great Britain?"])))
# print(clf.predict(count_vect.transform(["Who is the president of the United States?"])))
# print(clf.predict(count_vect.transform(["Where was he born?"])))

# test_questions = []
# test_labels = []
# with open('/questions/test_questions.csv', 'r') as test_csv:
#   csvreader = csv.reader(test_csv, delimiter=',')
#   # extracting field names through first row 
#   line_count = 0 
#   for row in csvreader:
#     if line_count == 0:
#       line_count += 1
#     else:
#       test_questions.append(row[0])
#       test_labels.append(row[1])
#       line_count += 1

# # Test Predictions
# predictions = clf.predict(count_vect.transform(test_questions))
# correct = 0
# for i in range(len(predictions)):
#   if predictions[i] == test_labels[i]:
#     correct += 1
#   print(f"Prediction: {predictions[i]} True: {test_labels[i]} Q:{test_questions[i]}")
# print(f"Got {correct} Correct out of {len(test_questions)} questions")
  
models = [
    #RandomForestClassifier(n_estimators=200, max_depth=3, random_state=0),
    LinearSVC(),
    #MultinomialNB(),
    LogisticRegression(random_state=0),
]
CV = 5
cv_df = pd.DataFrame(index=range(CV * len(models)))
entries = []
for model in models:
  model_name = model.__class__.__name__
  accuracies = cross_val_score(model, features, labels, scoring='accuracy', cv=CV)
  for fold_idx, accuracy in enumerate(accuracies):
    entries.append((model_name, fold_idx, accuracy))
cv_df = pd.DataFrame(entries, columns=['model_name', 'fold_idx', 'accuracy'])


print("== Mean Cross Validation Accuracy ==")
print(cv_df.groupby('model_name').accuracy.mean())