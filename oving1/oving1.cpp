#include "oving1.h"
#include <iostream>
#include <thread>
#include <cmath>
#include <vector>

using namespace std;

bool isPrime(int n);

int o1_main() {
    cout << "TDAT2004 - Øving 1" << endl;

    int start, end, threadCount;
    vector<int> range, threadRange, primeNumbers;
    mutex thread_mutex;

    // Prompts the user for variables
    /*
    cout << "Enter start number: " << endl;
    cin >> start;
    cout << "Enter a end number: " << endl;
    cin >> end;
    cout << "Enter thread count: " << endl;
    cin >> threadCount;
     */

    start = 1; end = 100; threadCount = 8;

    int rangeSize = end - start + 1;

    // Put all numbers to check into a list
    for (int i = 0; i < rangeSize; i++) {
        range.push_back(start++);
    }

    // Set threads to rangeSize if there are more threads entered.
    if (threadCount > rangeSize) {
        cout << "Too many threads! - Only " << rangeSize << " will be used" << endl;
        threadCount = rangeSize;
    }

    // Determine size for each individual size.
    int threadRangeSize = ceil((double) rangeSize / threadCount);
    cout << "Thread Range Size: " << threadRangeSize << endl;

    // Create threads
    vector<thread> myThreads;
    int counter = 0;
    int currentThread = 0;

    for (int i : range) {
        threadRange.push_back(i);
        counter++;
        if (counter == threadRangeSize || i == range.size()) {
            currentThread++;
            counter = 0;
            myThreads.emplace_back([currentThread, threadRange, &thread_mutex, &primeNumbers] {
                thread_mutex.lock();
                cout << "---Thread: " << currentThread << "---" << endl;
                for (int n : threadRange) {
                    cout << n << " ";
                }
                cout << endl;
                cout << "Prime numbers: " << endl;
                vector<int> range = threadRange;
                while (!(range.empty())) {
                    int numberToTest = range.back();
                    range.pop_back();
                    if (isPrime(numberToTest)) {
                        primeNumbers.push_back(numberToTest);
                        cout << numberToTest << " ";
                    }
                }
                cout << "\nThread " << currentThread << " - Done" << endl;
                thread_mutex.unlock();
            });
            threadRange.clear();
        }
    }

    // Join threads
    for (int i = 0; i < threadCount; i++) {
        myThreads[i].join();
    }

    // Sort the numbers
    sort(primeNumbers.begin(), primeNumbers.end());

    // Print out the array of prime numbers
    cout << endl << "Prime numbers in ascending order: " << endl;
    for (int n : primeNumbers)
        cout << n << " ";
    cout << endl;
    return 0;
}

// Function to find out if a number is prime or not
bool isPrime(int n) {
    if (n <= 1) {
        return false;
    }

    for (int i = 2; i <= n / 2; i++) {
        if (n % i == 0) {
            return false;
        }
    }
    return true;
}